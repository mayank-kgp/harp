package fascia;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Java version of fascia shared-memory mode http://fascia-psu.sourceforge.net
 *
 * In order to refer to the original C++ version directly, the code doesn't follow the JAVA convention.
 */
public class Fascia {


    private void read_in_graph(Graph g, String graph_file, boolean labeled,
                              int[] srcs_g, int[] dsts_g, int[] labels_g){

        try{
            Path graph_path = Paths.get(graph_file);
            InputStream in = Files.newInputStream(graph_path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            int n_g, m_g;

            n_g = Integer.parseInt( reader.readLine() );
            m_g = Integer.parseInt( reader.readLine() );

            srcs_g = new int[m_g];
            dsts_g = new int[n_g];

            if( labeled){
                labels_g = new int[n_g];
                for(int i = 0; i < n_g; ++i){
                    labels_g[i] = Integer.parseInt( reader.readLine() );
                }
            }else{
                labels_g = null;
            }

            for(int i = 0; i < m_g; ++i){
                srcs_g[i] =  Integer.parseInt( reader.readLine() );
                dsts_g[i] = Integer.parseInt( reader.readLine() );
            }

            in.close();

            g.init(n_g, m_g, srcs_g, dsts_g);

        } catch (IOException x) {
            System.err.println(x);
        }

    }

    public Fascia(String[] args){
        FasciaOptions foption = new FasciaOptions(args);

        String graph_file = foption.graph_file;
        String template_file = foption.template_file;
        String batch_file = foption.batch_file;
        int iterations = foption.iterations;
        boolean do_outerloop = foption.do_outerloop;
        boolean calculate_automorphism = foption.calculate_automorphism;
        boolean labeled = foption.labeled;
        boolean do_gdd = foption.do_gdd;
        boolean do_vert = foption.do_vert;
        boolean verbose = foption.verbose;
        int motif = foption.motif;
        boolean timing = foption.timing;

        if(motif != 0){
            System.err.println("run_motif not implemented yet");
        }else if( template_file != null){
            run_single(graph_file, template_file, labeled, do_vert, do_gdd, iterations,
                    do_outerloop, calculate_automorphism, verbose);
        }else if (batch_file != null){
            System.err.println("run_batch not implemented yet");
        }

    }

    private void run_single(String graph_file, String template_file, boolean labeled, boolean do_vert, boolean do_gdd,
                            int iterations, boolean do_outerloop, boolean calculate_automorphism, boolean verbose) {

    }


    public static void main(String[] args){

        Fascia fascia = new Fascia(args);

    }
}
