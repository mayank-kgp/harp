package fascia;

/**
 * Java version of fascia shared-memory mode http://fascia-psu.sourceforge.net
 */
public class fascia {

    public void print_info_short(String name){
        System.out.println("\nTo run: "+ name +" [-g graphfile] [-t template || -b batchfile] [options]\n"));
        System.out.println("Help: " + name + " -h\n\n");
        exit(0);
    }


    public void read_in_graph(Graph g, String graph_file, bool labeled,
                              int[] srcs_g, int[] dsts_g, int[] labels_g){



    }

}
