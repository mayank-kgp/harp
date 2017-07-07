set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
set xtics ("CL0" 0, "CL1" 1, "CL2" 2, "CL3" 3, "CL4" 4, "CL5" 5, "CL6" 6, "CL7" 7, "CL8" 8, "CL9" 9)
set xlabel "graph"
set ylabel "running time (sec)"
set xrange[-0.5:9.5]
set yrange[130:150]
set key top right

plot 'harp-degree2-miami-cl.data' using ($1):($2) title "even-partition" w lp lw 4, \
    'harp-degree2-miami-cl.data' using ($1):($3) title "deg-partition" w lp lw 4

set output 'harp-degree2-miami-cl.eps'
replot

