set terminal postscript eps enhanced monochrome dashed "Times-Roman" 25
set size 1,1
set xtics ("2" 2, "4" 4, "8" 8, "16" 16)
set xlabel "# computing nodes"
set ylabel "running time (hr)"
set xrange[1:32]
set yrange[0:5]
set log x
set key top right

plot 'harp-degree2-nyc.data' using ($1):($2) title "even-u3-1" w lp lw 4, \
    'harp-degree2-nyc.data' using ($1):($3) title "degree-u3-1" w lp lw 4, \
    'harp-degree2-nyc.data' using ($1):($4) title "even-u5-1" w lp lw 4, \
    'harp-degree2-nyc.data' using ($1):($5) title "degree-u5-1" w lp lw 4, \
    'harp-degree2-nyc.data' using ($1):($6) title "even-u7-1" w lp lw 4, \
    'harp-degree2-nyc.data' using ($1):($7) title "degree-u7-1" w lp lw 4


set output 'harp-degree2-nyc.eps'
replot

