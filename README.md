# Vico

> Vico is an Entity-Component-System (ECS) based co-simulation framework

#### FMI & SSP support

Support for FMI and SSP is provided by the __fmi__ module.

#### Time-series and XY charts

Support for plotting is provided by the __chart__ module.

#### 3D rendering

Generic 3D visualization components are provided by the __render-components__ module.
An implementation of a rendering system, relying on JMonkeyEngine, is provided by the __jme-renderer__ module.

#### Physics

Generic physics components are provided by the __physics-components__ module.
An implementation of a physics engine, relying on Bullet, is provided by the __bullet-physics__ module. 

### Command line interface
````bash
Usage: <main class> [-h] [COMMAND]
  -h, --help   display a help message
Commands:
  run           Run a Vico script
  simulate-fmu  Simulate a single FMU
  simulate-ssp  Simulate a co-simulation system described using SSP
````
