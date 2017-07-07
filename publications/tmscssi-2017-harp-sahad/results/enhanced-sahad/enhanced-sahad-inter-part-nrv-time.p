set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
set xtics ("nrv" 1, "nrv20" 2, "nrv40" 3, "nrv60" 4, "nrv80" 5, "nrv100" 6)
set xlabel "graphs"
set ylabel "running time (sec)"
set xrange[0.5:6.5]
set yrange[0:500]
#set log y
set key top right

plot 'enhanced-sahad-inter-part-nrv-time.data' using ($1-0.15):($2):(0.3) title "Random" w boxes fs pattern border -1, \
    'enhanced-sahad-inter-part-nrv-time.data' using ($1+0.15):($3):(0.3) title "Min-Cut" w boxes fs pattern border -1

set output 'enhanced-sahad-inter-part-nrv-time.eps'

replot

