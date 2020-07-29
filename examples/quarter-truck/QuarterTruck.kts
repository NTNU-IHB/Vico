#!vico run

arrayOf(
    "simulate-ssp",
    "QuarterTruck.ssp",
    "--stepSize", "1e-2",
    "--stopTime", "10",
    "-log", "extra/LogConfig.xml",
    "-chart", "extra/ChartConfig.xml",
    "-p", "initialValues",
    "-res", "results"
).cliExec()


