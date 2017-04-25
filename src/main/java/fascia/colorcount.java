package fascia;

import org.apache.log4j.Logger;

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

        int num_auto = calculate_automorphisms ? count_automorphisms(t): 1;
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
        return 0.0;
    }


    private void create_tables(){
        choose_table = init_choose_table(num_colors);
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




}
