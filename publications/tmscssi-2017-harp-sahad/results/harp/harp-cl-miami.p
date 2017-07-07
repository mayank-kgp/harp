set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
set xtics ("CL1" 1, "CL2" 2, "CL3" 3, "CL4" 4, "CL5" 5, "CL6" 6, "CL7" 7, "CL8" 8, "CL9" 9)
set xlabel "graph"
set ylabel "running time (sec)"
set xrange[0.5:9.5]
set yrange[130:150]
set key top right

plot 'harp-cl-miami.data' using ($1):($2) title "Miami" w lp lw 4

set output 'harp-cl-miami.eps'
replot

