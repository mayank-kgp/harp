set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
set xtics ("rnet" 1, "rnet20" 2, "rnet40" 3, "rnet60" 4, "rnet80" 5, "rnet100" 6)
set xlabel "graphs"
set ylabel "running time (sec)"
set xrange[0.5:6.5]
set yrange[0:500]
#set log x
set key top right

plot 'enhanced-sahad-sorting-rnet.data' using ($1-0.15):($2):(0.3) title "SAHad" w boxes fs pattern border -1, \
    'enhanced-sahad-sorting-rnet.data' using ($1+0.15):($3):(0.3) title "Enhanced-SAHad" w boxes fs pattern border -1

set output 'enhanced-sahad-sorting-rnet.eps'

replot

