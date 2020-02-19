reset
set terminal pdf enhanced dashed
#set terminal pdf enhanced color dashed dl 0.5 font "Helvetica,12"
set output 'd2d-go.pdf'
set datafile separator ';'

set xlabel 'Time(s)'
set ylabel 'File Size(MB)'

set key left top
#set xtics rotate by 270

set xrange[0:170]
set yrange[0:650]

set style line 1 lc rgb 'blue' lt 1 lw 3 dashtype 1;
set style line 2 lc rgb 'red' lt 2 lw 3 ps 1 dashtype 2;
set style line 3 lc rgb 'green' lt 3 lw 3 ps 1 dashtype 3;
set style line 4 lc rgb 'dark-yellow' lt 4 lw 3 ps 1 dashtype 4;
set style line 5 lc rgb 'pink' lt 5 lw 3 ps 1 dashtype 5;
set style line 6 lc rgb 'orange' lt 6 lw 3 ps 1 dashtype 6;

plot 'data/go.csv' using ($4/1000):($3/1048576) w l ls 1  title '0m', \
     'data/go.csv' using ($5/1000):($3/1048576) w l ls 2  title '1m', \
     'data/go.csv' using ($6/1000):($3/1048576) w l ls 3  title '3m', \
     'data/go.csv' using ($7/1000):($3/1048576) w l ls 4 title '5m', \
     'data/go.csv' using ($8/1000):($3/1048576) w l ls 5 title '10m', \
     'data/go.csv' using ($9/1000):($3/1048576) w l ls 6 title '15m'

reset
set terminal pdf enhanced dashed
#set terminal pdf enhanced color dashed dl 0.5 font "Helvetica,12"
set output 'd2d-client.pdf'
set datafile separator ';'

set xlabel 'Time(s)'
set ylabel 'File Size(MB)'

set key left top
#set xtics rotate by 270

set xrange[0:170]
set yrange[0:650]

set style line 1 lc rgb 'blue' lt 1 lw 3 dashtype 1;
set style line 2 lc rgb 'red' lt 2 lw 3 ps 1 dashtype 2;
set style line 3 lc rgb 'green' lt 3 lw 3 ps 1 dashtype 3;
set style line 4 lc rgb 'dark-yellow' lt 4 lw 3 ps 1 dashtype 4;
set style line 5 lc rgb 'pink' lt 5 lw 3 ps 1 dashtype 5;
set style line 6 lc rgb 'orange' lt 6 lw 3 ps 1 dashtype 6;

plot 'data/client.csv' using ($4/1000):($3/1048576) w l ls 1  title '0m', \
     'data/client.csv' using ($5/1000):($3/1048576) w l ls 2  title '1m', \
     'data/client.csv' using ($6/1000):($3/1048576) w l ls 3  title '3m', \
     'data/client.csv' using ($7/1000):($3/1048576) w l ls 4 title '5m', \
     'data/client.csv' using ($8/1000):($3/1048576) w l ls 5 title '10m', \
     'data/client.csv' using ($9/1000):($3/1048576) w l ls 6 title '15m'

#reset
#set terminal pdf enhanced dashed
#set output 'd2d-go-lb.pdf'
#set datafile separator ';'

#set xlabel 'Bandwidth(KB/s)'
#set ylabel 'File Size(KB)'

#set key left top

#set xrange[0:170]
#set yrange[0:650]

#set style line 1 lc rgb 'blue' lt 1 lw 3 dashtype 1;
#set style line 2 lc rgb 'red' lt 2 lw 3 ps 1 dashtype 2;
#set style line 3 lc rgb 'green' lt 3 lw 3 ps 1 dashtype 3;
#set style line 4 lc rgb 'dark-yellow' lt 4 lw 3 ps 1 dashtype 4;
#set style line 5 lc rgb 'pink' lt 5 lw 3 ps 1 dashtype 5;
#set style line 6 lc rgb 'orange' lt 6 lw 3 ps 1 dashtype 6;

#plot 'data/go.csv' using ($3/($4/1000)/1024):($3/1048576) w l ls 1  title '0m', \
#     'data/go.csv' using ($3/($5/1000)/1024):($3/1048576) w l ls 2  title '1m', \
#     'data/go.csv' using ($3/($6/1000)/1024):($3/1048576) w l ls 3  title '3m', \
#     'data/go.csv' using ($3/($7/1000)/1024):($3/1048576) w l ls 4 title '5m', \
#     'data/go.csv' using ($3/($8/1000)/1024):($3/1048576) w l ls 5 title '10m', \
#     'data/go.csv' using ($3/($9/1000)/1024):($3/1048576) w l ls 6 title '15m'

#((tamanho do arquivo)/(tempo/1000) ) /1024

##########d2d-go-lb 2
reset
set terminal pdf enhanced dashed dl 0.5
set output 'd2d-go-lb.pdf'
set datafile separator ';'

set xlabel 'File Size (KB)'
set ylabel 'Bandwidth (KB/s)'

set key right bottom

set style lines 1 lc rgb 'blue' lt 1 lw 3 dashtype 1;
set style lines 2 lc rgb 'red' lt 2 lw 3 ps 1 dashtype 2;
set style lines 3 lc rgb 'green' lt 3 lw 3 ps 1 dashtype 3;
set style lines 4 lc rgb 'dark-yellow' lt 4 lw 3 ps 1 dashtype 4;
set style lines 5 lc rgb 'pink' lt 5 lw 3 ps 1 dashtype 5;
set style lines 6 lc rgb 'orange' lt 6 lw 3 ps 1 dashtype 6;


plot 'data/go.csv' using ($3/1024):($3/($4/1000)/1024) w lines ls 1  title '0m', \
     'data/go.csv' using ($3/1024):($3/($5/1000)/1024) w lines ls 2  title '1m', \
     'data/go.csv' using ($3/1024):($3/($6/1000)/1024) w lines ls 3  title '3m', \
     'data/go.csv' using ($3/1024):($3/($7/1000)/1024) w lines ls 4 title '5m', \
     'data/go.csv' using ($3/1024):($3/($8/1000)/1024) w lines ls 5 title '10m', \
     'data/go.csv' using ($3/1024):($3/($9/1000)/1024) w lines ls 6 title '15m'
