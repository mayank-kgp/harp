set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
set xtics ("CL-1" 1, "CL-2" 2, "CL-3" 3, "CL-4" 4, "CL-5" 5)
set xlabel "Graph"
set ylabel "running time (sec)"
set xrange[0.5:5.5]
set yrange[30:600]
#set log y
set key top right

plot 'harp-varying-edge-cl.data' using ($1-0.2):($2):(0.2) title "Miami" w lp lw 4


set output 'harp-varying-edge-cl.eps'
replot

