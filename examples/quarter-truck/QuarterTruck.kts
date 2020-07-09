#!vico run

arrayOf(
    "simulate-ssp",
    "quarter-truck.ssp",
    "--stepSize", "1e-2",
    "--stopTime", "10",
    "-log", "extra/LogConfig.xml",
    "-chart", "extra/ChartConfig.xml",
    "-res", "results"
).cliExec()



