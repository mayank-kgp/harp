set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
set xtics ("Miami" 1, "Web-Google" 3)
set xlabel "Graphs"
set ylabel "running time (sec)"
set xrange[0:4]
set yrange[30:2000]
set log y
set key top right

plot 'harp-vs-sahad-40-threads.data' using ($1-0.3):($2):(0.2) title "Harp u5-1" w boxes fs pattern, \
     'harp-vs-sahad-40-threads.data' using ($1-0.1):($3):(0.2) title "SAHad u5-1" w boxes fs pattern, \
     'harp-vs-sahad-40-threads.data' using ($1+0.1):($4):(0.2) title "Harp  u7-1" w boxes fs pattern, \
     'harp-vs-sahad-40-threads.data' using ($1+0.3):($5):(0.2) title "SAHad u7-1" w boxes fs pattern


set output 'harp-vs-sahad-40-threads.eps'
replot

