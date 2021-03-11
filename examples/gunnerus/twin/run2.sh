vico simulate-ssp \
-dt "0.05" \
--stopTime "2000" \
-log "LogConfig2.xml" \
-chart "chart.main.kts" \
-s "scenario.main.kts" \
-p "initialValues" \
-res "results/data2" \
"gunnerus-twin2.ssp"
read -p "Press enter to continue"
