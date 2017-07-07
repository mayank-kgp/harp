set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
set xtics ("nrv" 1, "nrv20" 2, "nrv40" 3, "nrv60" 4, "nrv80" 5, "nrv100" 6)
set xlabel "graphs"
set ylabel "running time (sec)"
set xrange[0.5:6.5]
set yrange[0:500]
#set log x
set key top right

plot 'enhanced-sahad-sorting-nrv.data' using ($1-0.15):($2):(0.3) title "SAHad" w boxes fs pattern border -1, \
    'enhanced-sahad-sorting-nrv.data' using ($1+0.15):($3):(0.3) title "Enhanced-SAHad" w boxes fs pattern border -1

set output 'enhanced-sahad-sorting-nrv.eps'

replot

