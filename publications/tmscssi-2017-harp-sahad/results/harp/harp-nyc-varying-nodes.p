set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
set xtics ("u3-1" 1, "u5-1" 2, "u7-1" 3)
set xlabel "templates"
set ylabel "running time (hr)"
set xrange[0.5:3.5]
set yrange[0:4]
set key top right

plot 'harp-nyc-varying-nodes.data' using ($1-0.3):($2):(0.2) title "2 nodes" w boxes fs pattern, \
'harp-nyc-varying-nodes.data' using ($1-0.1):($3):(0.2) title "4 nodes" w boxes fs pattern, \
'harp-nyc-varying-nodes.data' using ($1+0.1):($4):(0.2) title "8 nodes" w boxes fs pattern, \
'harp-nyc-varying-nodes.data' using ($1+0.3):($5):(0.2) title "16 nodes" w boxes fs pattern


set output 'harp-nyc-varying-nodes.eps'
replot

