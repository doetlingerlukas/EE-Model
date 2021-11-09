![Apollo-Core CI Java Repository](https://github.com/Apollo-Core/EE-Model/workflows/Apollo-Core%20CI%20Java%20Repository/badge.svg)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5fac809da11b41199530c92460ce00fd)](https://app.codacy.com/gh/Apollo-Core/EE-Model?utm_source=github.com&utm_medium=referral&utm_content=Apollo-Core/EE-Model&utm_campaign=Badge_Grade_Settings)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/dd5e2d80150c466f9f3b6f68bbb31057)](https://www.codacy.com/gh/Apollo-Core/EE-Model/dashboard?utm_source=github.com&utm_medium=referral&utm_content=Apollo-Core/EE-Model&utm_campaign=Badge_Coverage)
[![](https://jitpack.io/v/Apollo-Core/EE-Model.svg)](https://jitpack.io/#Apollo-Core/EE-Model)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# EE-Model

Repository implementing Apollo's graph-based system model (application, resources, mapping, and routing) and the services used to access the attributes of the graph elements (graph nodes and edges). 

## Relevance

### Repository relevant if

+ You want to understand how Apollo models and orchestrates applications (see [EE-Control](https://github.com/Apollo-Core/EE-Control) for details on the dynamic operations and the orchetration model)
+ You want to extend the model by defining additional element properties or new types of nodes/edges

### Repository less relevant if

+ You want to use Apollo for application orchestration ([EE-Demo](https://github.com/Apollo-Core/EE-Demo) is probably a good place to start; the readme of [EE-IO](https://github.com/Apollo-Core/EE-IO) contains a description of the format of the input files required by Apollo)
+ You want implement a particular type of component, such as a scheduler (see [SC-Core](https://github.com/Apollo-Core/SC-Core)) or a new way of enacting functions

## Relations to other parts of Apollo-Core

### Depends On
  + EE-Core

### Used By
  + EE-IO
  + EE-Enactables
  + SC-Core
  + EE-Control
  + EE-Visualization
  + EE-Demo
  + EE-Deploy
  + EE-Docker
