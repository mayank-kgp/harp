set terminal postscript eps enhanced color "Times-Roman" 25
set size 1,1
set xtics ("u3-1" 1, "u5-1" 2, "u5-2" 3, "u5-3" 4, "u7-1" 5)
set xlabel ""
set ylabel "running time (sec)"
set xrange[0.5:5.5]
set yrange[0:400]
#set log y
set key top right

plot 'tmp.data' using ($2-0.2):($3):(0.18) title "min-cut partitioning" w  0.9, \
     'roadNet-CA.u5-1.8.min-cut-vs-random.data' using ($2+0.2):($4):(0.18) title "random partitioning" w boxes fs solid 0.9

#plot  'label-count-time.data' using ($1-0.25):3:(0.2) title "L7-1" w boxes fs pattern border -1, \
#      'label-count-time.data' using ($1):5:(0.2) title "L10-1" w boxes fs pattern border -1, \
#      'label-count-time.data' using ($1+0.25):7:(0.2) title "L12-1" w boxes fs pattern border -1


#set output 'nrv.min-cut-vs-random.eps'
set output 'rNet.min-cut-vs-random.eps'
replot

