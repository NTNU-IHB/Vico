vico simulate-ssp \
-dt "0.05" \
--stopTime "2000" \
-log "LogConfig.xml" \
-chart "ChartConfig.xml" \
-s "scenario.main.kts" \
-p "initialValues" \
-res "results" \
"gunnerus-twin.ssp"
read -p "Press enter to continue"
