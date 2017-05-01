# Analysis
###### Jenna Sgarlata (sgarlatajj@msoe.edu) and Andrew Sullivan-Bormann (sullivan-bormannaj@msoe.edu)
| Original Line Number | Fault Description | Bug Description |
| 84 | not throwing a NullPointerException when a null AudioPlayer was passed into constructor | null AudioPlayer could be passed in |
| 119 | checking for percent change greater than zero, when it should be checking for greater than or equal to 1 | AudioPlayer would play when percent change was between 0 and 1, when it should not be |
| 151 | throwing exception when the current quote was not null, rather than when it was | exception was thrown when state was valid |
