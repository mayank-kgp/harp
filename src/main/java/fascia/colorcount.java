package fascia;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Random;

public class colorcount {
    final Logger LOG = Logger.getLogger(colorcount.class);

    Graph g;
    Graph t;
    int[] labels_g;
    int[] labels_t;
    int[] colors_g;
    boolean labeled;

    Graph[] subtemplates;
    int subtemplate_count;
    int num_colors;
    int num_iter;
    int cur_iter;

    dynamic_table_array dt;
    partitioner part;

    int[][] choose_table;
    int[][][][] index_sets;
    int[][][][][] color_sets;
    int[][][][] comb_num_indexes;
    int[][] comb_num_indexes_set;
    int[] num_verts_table;
    int num_verts_graph;
    int max_degree;

    double[] final_vert_counts;
    boolean do_graphlet_freq;
    boolean do_vert_output;
    boolean calculate_automorphisms;
    boolean verbose;

    int set_count;
    int total_count;
    int read_count;

    void init(Graph full_graph, boolean calc_auto, boolean do_gdd, boolean do_vert, boolean verb){
        g = full_graph;
        num_verts_graph = g.num_vertices();
        labels_g = g.labels;
        labeled = g.labeled;
        do_graphlet_freq = do_gdd;
        do_vert_output = do_vert;
        calculate_automorphisms = calc_auto;
        verbose = verb;
        dt = new dynamic_table_array();

        if( do_graphlet_freq || do_vert_output){
            //Skipped
        }
    }

    double do_full_count(Graph template, int N){
        num_iter = N;
        t = template;

        labels_t = t.labels;

        if(verbose){
            LOG.info("Begining partition...");
        }

        part = new partitioner(t, labeled, labels_t);
        part.sort_subtemplates();

        if(verbose){
            LOG.info("done partitioning");
        }

        num_colors = t.num_vertices();
        subtemplates = part.get_subtemplates();
        subtemplate_count = part.get_subtemplate_count();

        create_tables();
        dt.init(subtemplates, subtemplate_count, g.num_vertices(), num_colors);

        //determine max out degree
        int max_out_degree = 0;
        for(int i =0; i < num_verts_graph; i++){
            int out_degree_i = g.out_degree(i);
            if(out_degree_i > max_out_degree){
                max_out_degree = out_degree_i;
            }
        }
        max_degree = max_out_degree;

        if(verbose){
            LOG.info("n "+ num_verts_graph + ", max degree " + max_out_degree);
        }

        //begin the counting
        double count = 0.0;
        for(cur_iter = 0; cur_iter < N; cur_iter++){
            long elt = 0;
            if(verbose){
                elt = System.currentTimeMillis();
            }
            count += template_count();
            if(verbose){
                elt = System.currentTimeMillis() - elt;
                LOG.info("Time for count: "+ elt +" ms");
            }
        }

        double final_count = count / (double) N;
        double prob_colorful = Util.factorial(num_colors) /
                ( Util.factorial(num_colors - t.num_vertices()) * Math.pow(num_colors, t.num_vertices()) );

        int num_auto = calculate_automorphisms ? Util.count_automorphisms(t): 1;
        final_count = Math.floor(final_count / (prob_colorful * num_auto) + 0.5);

        if(verbose){
            LOG.info("Probability colorful: " + prob_colorful);
            LOG.info("Num automorphisms: " + num_auto );
            LOG.info("Final count: " + final_count);
        }

        if(do_graphlet_freq || do_vert_output){
            System.err.println("do_gdd or do_vert skipped");
        }

        delete_tables();
        part.clear_temparrays();
        return final_count;
    }

    double[] get_vert_counts(){
        return final_vert_counts;
    }

    // This does a single counting for a given templates
    //Return the full scaled count for the template on the whole graph
    private double template_count(){
        //do random coloring for full graph

        Random rand = new Random();

        int num_verts = g.num_vertices();
        colors_g = new int[num_verts];

        for(int v = 0; v < num_verts; ++v){
            colors_g[v] = rand.nextInt(num_colors+1) ;
        }

        //start doing the counting, starting at bottom of partition tree
        // then go through all of the subtemplates except for the primary
        // since that's handled a bt differently
        for( int s = subtemplate_count -1; s > 0; --s){
            set_count = 0;
            total_count = 0;
            read_count = 0;
            int num_verts_sub = num_verts_table[s];

            if(verbose)
                LOG.info("Initing with sub "+ s + ", verts: " + num_verts_sub );

            int a = part.get_active_index(s);
            int p = part.get_passive_index(s);
            dt.init_sub(s, a, p);

            long elt = 0;
            if( num_verts_sub == 1){
                if(verbose){
                    elt = System.currentTimeMillis();
                }
                init_table_node(s);
                if(verbose){
                    elt = System.currentTimeMillis() - elt;
                    LOG.info("s " + s +", init_table_node "+ elt + " ms");
                }
            }else{
                if(verbose){
                    elt = System.currentTimeMillis();
                }
                colorful_count(s);

                if(verbose){
                    elt = System.currentTimeMillis() - elt;
                    LOG.info("s " + s +", array time "+ elt + " ms");
                }
            }

            if(verbose){
                 if( num_verts != 0){
                     double ratio1  =(double) set_count / (double) num_verts;
                     double ratio2 = (double) read_count /(double) num_verts;
                     LOG.info("  Sets: "+ set_count + " Total: "+num_verts + "  Ratio: " + ratio1);
                     LOG.info("  Reads: "+read_count + " Total: "+num_verts + "  Ratio: " + ratio2);
                }else{
                    LOG.info("  Sets: "+ set_count + " Total: "+num_verts );
                    LOG.info("  Reads: "+read_count + " Total: "+num_verts);
                }
            }
            if( a != Constants.NULL_VAL)
                dt.clear_sub(a);
            if(p != Constants.NULL_VAL)
                dt.clear_sub(p);
        }

        if(verbose)
            LOG.info("Done with initialization. Doing full count");

        // do the count for the full template
        float full_count = 0;
        set_count = 0;
        total_count = 0;
        read_count = 0;
        long elt = 0;

        int a = part.get_active_index(0);
        int p = part.get_passive_index(0);
        dt.init_sub(0, a, p);

        if(verbose) elt= System.currentTimeMillis();
        full_count = colorful_count(0);

        if(verbose){
            elt = System.currentTimeMillis() - elt;
            LOG.info("s 0, array time " + elt + "ms");
        }
        colors_g = null;
        dt.clear_sub(a);
        dt.clear_sub(p);

        if(verbose){
            if( num_verts != 0){
                double ratio1  =(double) set_count / (double) num_verts;
                double ratio2 = (double) read_count /(double) num_verts;
                LOG.info("  Non-zero: "+ set_count + " Total: "+num_verts + "  Ratio: " + ratio1);
                LOG.info("  Reads: "+read_count + " Total: "+num_verts + "  Ratio: " + ratio2);
            }else{
                LOG.info("  Non-zero: "+ set_count + " Total: "+num_verts );
                LOG.info("  Reads: "+read_count + " Total: "+num_verts);
            }
            LOG.info("Full count: " + full_count);
        }

        return (double)full_count;
    }


    private void init_table_node(int s){
        int set_count_loop = 0;
        if( !labeled){
            for (int v = 0; v < num_verts_graph; ++v) {
                int n = colors_g[v];

                dt.set(v, comb_num_indexes_set[s][n], 1.0f);
                set_count_loop++;
            }
        }else{
            int[] labels_sub = part.get_labels(s);
            int label_s = labels_sub[0];
            for (int v = 0; v < num_verts_graph; ++v){
                int n = colors_g[v];
                int label_g = labels_g[v];
                if (label_g == label_s) {
                    dt.set(v, comb_num_indexes_set[s][n], 1.0f);
                    set_count_loop++;
                }
            }
        }
        set_count = set_count_loop;
    }


    private float colorful_count(int s){
        float cc = 0.0f;
        int num_verts_sub = subtemplates[s].num_vertices();

        int active_index = part.get_active_index(s);
        int num_verts_a = num_verts_table[active_index];
        int num_combinations = choose_table[num_verts_sub][num_verts_a];
        int set_count_loop = 0;
        int total_count_loop = 0;
        int read_count_loop = 0;

        long elt = System.currentTimeMillis();

        int[] valid_nbrs = new int[max_degree];
        assert(valid_nbrs != null);
        int valid_nbrs_count = 0;
        for(int v = 0; v < num_verts_graph; ++v){
            valid_nbrs_count = 0;

            if( dt.is_vertex_init_active(v)){
                int[] adjs = g.adjacent_vertices(v);
                int end = g.out_degree(v);
                float[] counts_a = dt.get_active(v);
                ++read_count_loop;
                for(int i = 0; i < end; ++i){
                    int adj_i = adjs[i];
                    if(dt.is_vertex_init_passive(adj_i)){
                        valid_nbrs[valid_nbrs_count++] = adj_i;
                    }
                }

                if(valid_nbrs_count != 0){
                    int num_combinations_verts_sub = choose_table[num_colors][num_verts_sub];

                    for(int n = 0; n < num_combinations_verts_sub; ++n){
                        float color_count = 0.0f;
                        int[] comb_indexes_a = comb_num_indexes[0][s][n];
                        int[] comb_indexes_p = comb_num_indexes[1][s][n];
                        int p = num_combinations -1;
                        for(int a = 0; a < num_combinations; ++a, --p){
                            float count_a = counts_a[comb_indexes_a[a]];
                            if( count_a > 0){
                                for(int i = 0; i < valid_nbrs_count; ++i){
                                    color_count += count_a * dt.get_passive(valid_nbrs[i], comb_indexes_p[p]);
                                    ++read_count_loop;
                                }
                            }
                        }

                        if( color_count > 0.0){
                            cc += color_count;
                            ++set_count_loop;

                            if(s != 0)
                                dt.set(v, comb_num_indexes_set[s][n], color_count);
                            else if(do_graphlet_freq || do_vert_output)
                                final_vert_counts[v] += (double)color_count;
                        }
                        ++total_count_loop;
                    }
                }
            }
        }
        elt = System.currentTimeMillis() - elt;
        //System.out.println("time: "+ elt +"ms");
        valid_nbrs = null;
        set_count = set_count_loop;
        total_count = total_count_loop;
        read_count_loop = read_count_loop;
        return cc;
    }

    private void create_tables(){
        choose_table = Util.init_choose_table(num_colors);
        create_num_verts_table();
        create_all_index_sets();
        create_all_color_sets();
        create_comb_num_system_indexes();
        delete_all_color_sets();
        delete_all_index_sets();
    }

    private void delete_tables(){
        for(int i = 0; i <= num_colors; ++i)
            choose_table[i] = null;
        choose_table = null;

        delete_comb_num_system_indexes();
        num_verts_table = null;
    }

    private void create_num_verts_table(){
        num_verts_table = new int[subtemplate_count];
        for(int s = 0; s < subtemplate_count; ++s){
            num_verts_table[s] = subtemplates[s].num_vertices();
        }
    }
    private void create_all_index_sets(){
        index_sets = new int[num_colors][][][];

        for(int i = 0; i < (num_colors -1 ); ++i){
            int num_vals = i + 2;
            index_sets[i] = new int[num_vals -1][][];
            for(int j = 0; j < (num_vals - 1); ++j){
                int set_size = j + 1;
                int num_combinations = Util.choose(num_vals, set_size);
                index_sets[i][j] = new int[num_combinations][];

                int[] set = Util.init_permutation(set_size);

                for(int k = 0; k < num_combinations; ++k){
                    index_sets[i][j][k] = new int[set_size];
                    for(int p = 0; p < set_size; ++p){
                        index_sets[i][j][k][p] = set[p] - 1;
                    }
                    Util.next_set(set, set_size, num_vals);
                }
                set = null;
            }
        }
    }


    private void create_all_color_sets(){
        color_sets = new int[subtemplate_count][][][][];

        for(int s = 0; s < subtemplate_count; ++s){
            int num_verts_sub = subtemplates[s].num_vertices();

            if( num_verts_sub > 1){
                int num_sets = Util.choose(num_colors, num_verts_sub);
                color_sets[s] = new int[num_sets][][][];

                int[] colorset = Util.init_permutation(num_verts_sub);
                for(int n = 0; n < num_sets; ++n){
                    int num_child_combs = num_verts_sub - 1;
                    color_sets[s][n] = new int[num_child_combs][][];

                    for(int c = 0; c < num_child_combs; ++c){
                        int num_verts_1 = c + 1;
                        int num_verts_2 = num_verts_sub - num_verts_1;
                        int[][] index_set_1 = index_sets[num_verts_sub-2][num_verts_1-1];
                        int[][] index_set_2 = index_sets[num_verts_sub-2][num_verts_2-1];

                        int num_child_sets = Util.choose(num_verts_sub, c+1);
                        color_sets[s][n][c] = new int[num_child_sets][];

                        for(int i = 0; i < num_child_sets; ++i){
                            color_sets[s][n][c][i] = new int[num_verts_sub];

                            for(int j = 0; j < num_verts_1; ++j)
                                color_sets[s][n][c][i][j] = colorset[index_set_1[i][j]];

                            for(int j = 0; j < num_verts_2; ++j)
                                color_sets[s][n][c][i][j+num_verts_1] = colorset[index_set_2[i][j]];
                        }
                    }
                    Util.next_set(colorset, num_verts_sub, num_colors);
                }
                colorset = null;
            }
        }
    }


    private void create_comb_num_system_indexes(){
        comb_num_indexes = new int[2][subtemplate_count][][];
        comb_num_indexes_set = new int[subtemplate_count][];

        for(int s = 0; s < subtemplate_count; ++s){
            int num_verts_sub = subtemplates[s].num_vertices();
            int num_combinations_s = Util.choose(num_colors, num_verts_sub);

            if( num_verts_sub > 1){
                comb_num_indexes[0][s] = new int[num_combinations_s][];
                comb_num_indexes[1][s] = new int[num_combinations_s][];
            }

            comb_num_indexes_set[s] = new int[num_combinations_s];
            int[] colorset_set = Util.init_permutation(num_verts_sub);

            for(int n = 0; n < num_combinations_s; ++n){
                comb_num_indexes_set[s][n] = Util.get_color_index(colorset_set, num_verts_sub);

                if( num_verts_sub > 1){
                    int num_verts_a = part.get_num_verts_active(s);
                    int num_verts_p = part.get_num_verts_passive(s);

                    int[] colors_a;
                    int[] colors_p;
                    int[][] colorsets = color_sets[s][n][num_verts_a-1];

                    int num_combinations_a= Util.choose(num_verts_sub, num_verts_a);
                    comb_num_indexes[0][s][n] = new int[num_combinations_a];
                    comb_num_indexes[1][s][n] = new int[num_combinations_a];

                    int p = num_combinations_a - 1;
                    for(int a = 0; a < num_combinations_a; ++a, --p){
                        colors_a = colorsets[a];
                        //are they the same?
                        // colors_p = colorsets[p] + num_verts_a;
                        colors_p = new int[num_verts_p];
                        System.arraycopy(colorsets[p], num_verts_a, colors_p, 0, num_verts_p);

                        int color_index_a = Util.get_color_index(colors_a, num_verts_a);
                        int color_index_p = Util.get_color_index(colors_p, num_verts_p);

                        comb_num_indexes[0][s][n][a] = color_index_a;
                        comb_num_indexes[1][s][n][p] = color_index_p;
                    }
                }
                Util.next_set(colorset_set, num_verts_sub, num_colors);
            }
            colorset_set = null;
        }
    }
    private void delete_comb_num_system_indexes(){
        for(int s = 0; s < subtemplate_count; ++s){
            int num_verts_sub = subtemplates[s].num_vertices();
            int num_combinations_s = Util.choose(num_colors, num_verts_sub);

            for(int n = 0; n < num_combinations_s; ++n){
                if(num_verts_sub > 1){
                    comb_num_indexes[0][s][n] = null;
                    comb_num_indexes[1][s][n] = null;
                }
            }

            if(num_verts_sub > 1){
                comb_num_indexes[0][s] = null;
                comb_num_indexes[1][s] = null;
            }

            comb_num_indexes_set[s]= null;
        }
        comb_num_indexes[0]= null;
        comb_num_indexes[1] = null;
        comb_num_indexes= null;
        comb_num_indexes_set = null;

    }


    private void delete_all_color_sets(){
        for(int s = 0; s < subtemplate_count; ++s){
            int num_verts_sub = subtemplates[s].num_vertices();
            if( num_verts_sub > 1) {
                int num_sets = Util.choose(num_colors, num_verts_sub);

                for (int n = 0; n < num_sets; ++n) {
                    int num_child_combs = num_verts_sub - 1;
                    for (int c = 0; c < num_child_combs; ++c) {
                        int num_child_sets = Util.choose(num_verts_sub, c + 1);
                        for (int i = 0; i < num_child_sets; ++i) {
                            color_sets[s][n][c][i] = null;
                        }
                        color_sets[s][n][c] = null;
                    }
                    color_sets[s][n] = null;
                }
                color_sets[s] = null;
            }
        }
        color_sets = null;
    }


    private void delete_all_index_sets(){

        for (int i = 0; i < (num_colors-1); ++i) {
            int num_vals = i + 2;
            for (int j = 0; j < (num_vals-1); ++j) {
                int set_size = j + 1;
                int num_combinations = Util.choose(num_vals, set_size);
                for (int k = 0; k < num_combinations; ++k) {
                    index_sets[i][j][k] = null;
                }
                index_sets[i][j] = null;
            }
            index_sets[i] = null;
        }
        index_sets = null;
    }


}
