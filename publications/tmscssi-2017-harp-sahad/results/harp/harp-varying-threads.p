set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
#set xtics ("1" 1, "2" 2, "4" 4, "8" 8, "16" 16)
set xlabel "# of threads"
set ylabel "running time (sec)"
set xrange[0:100]
set yrange[20:500]
#set log x
set key top right

plot 'harp-varying-threads.data' using ($1):($2) title "Harp u5-1 Miami" w lp lw 4, \
     'harp-varying-threads.data' using ($1):($3) title "Harp u7-1 Miami" w lp lw 4, \
     'harp-varying-threads.data' using ($1):($4) title "Harp u5-1 Web-Google" w lp lw 4, \
     'harp-varying-threads.data' using ($1):($5) title "Harp u7-1 Web-Google" w lp lw 4


set output 'harp-varying-threads.eps'
replot

