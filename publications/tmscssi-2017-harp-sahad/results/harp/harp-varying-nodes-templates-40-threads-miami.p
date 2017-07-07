set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
set xtics ("1" 1, "2" 2, "4" 4, "8" 8, "16" 16)
set xlabel "# computing nodes"
set ylabel "running time (sec)"
set xrange[1:32]
set yrange[50:500]
set log x
set key top right

plot 'harp-varying-nodes-templates-40-threads-miami.data' using ($1):($2) title "u3-1" w lp lw 4, \
     'harp-varying-nodes-templates-40-threads-miami.data' using ($1):($3) title "u5-1" w lp lw 4, \
     'harp-varying-nodes-templates-40-threads-miami.data' using ($1):($4) title "u5-2" w lp lw 4, \
     'harp-varying-nodes-templates-40-threads-miami.data' using ($1):($5) title "u5-3" w lp lw 4, \
     'harp-varying-nodes-templates-40-threads-miami.data' using ($1):($6) title "u7-1" w lp lw 4


set output 'harp-varying-nodes-templates-40-threads-miami.eps'
replot

