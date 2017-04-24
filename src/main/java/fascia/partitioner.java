package fascia;


import java.util.List;

public class partitioner {

    private Graph[] subtemplates_create;
    private Graph[] subtemplates;
    private Graph subtemplate;

    private List<Integer> active_children;
    private List<Integer> passive_children;
    private List<Integer> parents;
    private List<Integer> cut_edge_labels;
    private List<int[]> label_maps;

    private int current_creation_index;
    private int subtemplate_count;

    private boolean[] count_needed;
    private boolean labeled;

    public partitioner(){}




    /**
     * Check nums 'closes the gaps' in the srcs and dsts arrays
     * Can't initialize a graph with edges (0,2),(0,4),(2,5)
     * This changes the edges to (0,1),(0,2),(1,3)
    */
    private void check_nums(int root, List<Integer> srcs, List<Integer> dsts, int[] labels, int[] labels_sub){
        int maximum = Util.get_max(srcs, dsts);
        int size = srcs.size();

        int[] mappings = new int[maximum + 1];

        for(int i = 0; i < maximum + 1; ++i){
            mappings[i] = -1;
        }

        int new_map = 0;
        mappings[root] = new_map ++;

        for(int i = 0; i < size; ++i){
            if( mappings[ srcs.get(i) ] == -1)
                mappings[ srcs.get(i) ] = new_map++;

            if( mappings[ dsts.get(i) ] == -1 )
                mappings[ dsts.get(i) ] = new_map++;
        }

        for(int i = 0; i < size; ++i){
            srcs.set(i, mappings[srcs.get(i)]);
            dsts.set(i, mappings[dsts.get(i)]);
        }

        if( labeled ){
            for (int i = 0; i < maximum; ++i){
                labels[ mappings[i] ] = labels_sub[i];
            }
        }
    }


    private void set_active_child(int s, int a){
        while( active_children.size() <= s)
            active_children.add(Constants.NULL_VAL);

        active_children.set(s, a);
    }


    private void set_passive_child(int s, int p){
        while( passive_children.size() <= s)
            passive_children.add(Constants.NULL_VAL);

        passive_children.set(s,p);
    }

    private void set_parent(int c, int p){
        while( parents.size() <= c )
            parents.add(Constants.NULL_VAL);

        parents.set(c, p);
    }


}
