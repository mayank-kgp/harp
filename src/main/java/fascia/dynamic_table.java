package fascia;

public abstract class dynamic_table {

    protected int[][] choose_table;
    protected int[] num_colorsets;

    protected Graph[] subtemplates;

    protected int num_colors;
    protected int num_subs;
    protected int num_verts;

    protected boolean is_inited;
    protected boolean [] is_sub_inited;


    public dynamic_table(){
        choose_table = null;
        num_colors = 0;
        num_verts = 0;
        num_subs = 0;

        is_inited = false;
        is_sub_inited = null;
    }

    public void free(){
        num_colorsets = null;
        choose_table = null;
    }

    public abstract void init(Graph[] subtemplates, int num_subtemplates, int num_vertices, int num_colors);

    public abstract void init_sub(int subtemplate);

    public abstract void clear_sub(int subtemplate);

    public abstract void clear_table();

    public abstract boolean is_init() ;

    public abstract boolean is_sub_init(int subtemplate);

    protected void init_choose_table(){
        choose_table = new int[num_colors + 1][num_colors + 1];

        for(int i = 0; i <= num_colors; ++i){
            for(int j = 0; j <= num_colors; ++j){
                choose_table[i][j] = Util.choose(i,j);
            }
        }
    }

    protected void init_num_colorsets(){
        num_colorsets = new int[num_subs];
        for(int s = 0; s < num_subs; ++s){
            num_colorsets[s] = Util.choose(num_colors, subtemplates[s].num_vertices());
        }
    }

}
