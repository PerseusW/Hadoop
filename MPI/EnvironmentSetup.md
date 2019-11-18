# MPI Environment Setup

OS Environment: Ubuntu 18.04.3

## 0 Quick Overview

1. Install via command line:

```shell
sudo apt-get install mpich
```

2. Verify installation:

```shell
which mpicc
/usr/bin/mpicc
```

## 1 Details

Please read all of it before actually doing anything so that you don't miss any tips that might help you.

The most famous implementation of `MPI`, `mpich`, can be installed via two approaches:

- Command line (Strongly recommended)
- Source code

More tips are given here:

### 1.1 Command Line

This is really friendly for beginners, and normally takes less time than compiling the source code. All you have to do is open the terminal in Ubuntu (**Ctrl+Alt+T**), and type in the following line:

```shell
sudo apt-get install mpich
```

You will be asked for your password because you are using `sudo` mode. The rest is up to your computer to do.

*Tip*: If you happen to be in China, I recommend that you get up early and use Ethernet before typing in this command. The Internet performs better at this time, otherwise the download process can be painful.

Verify installation by typing in:

```shell
which mpicc
```

This is asking your computer to tell you which `MPI` compiler you are currently using.

And you should get the feedback:

```shell
/usr/bin/mpicc
```

### 1.2 Source Code

Please *Bing* it yourself.