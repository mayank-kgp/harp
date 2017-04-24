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

    double do_full_count(Graph template, int iterations){
        num_iter = iterations;
        t = template;
        labels_t = t.labels;

        if(verbose){
            LOG.info("Begining partition...");
        }
        //LINE 74~




        return 0.0;
    }
}
