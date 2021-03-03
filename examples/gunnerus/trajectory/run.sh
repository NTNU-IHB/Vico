vico simulate-ssp \
-dt "0.05" \
--stopTime "2000" \
-log "LogConfig.xml" \
-visual "VisualConfig.xml" \
-chart "ChartConfig.xml" \
-s "scenario.main.kts" \
-p "initialValues" \
-res "results" \
-port 8000 \
"gunnerus-trajectory.ssp"
read -p "Press enter to continue"
