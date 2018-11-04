# Asigment report

## **1. Project overveiw**
Java application based on JavaFx lib. that is GUI app providing graph represantation of 3 numerical methods of solving first order linear differential equation via:
- Euler's method
- Improved Euler's method
- Runge-kutta method

## **2. The purpoce of the project**
Easy and fast finding numerical solution of given DE. Userfriendly inteface via wich one have to define IVP, interval on wich we are looking for solution, step and etc.

## **3. Feedback**

### Brainstorming
When i got the requirements, i was confused about what to do first. I decided to work with Java because i am used to it.
Then i was confused about requirement, because they were not clear ¯\_(ツ)_/¯

### First steps
Before some programming, i was studing numerical methods. I found out that all Euler's and Improved Euler's methods are Runge-Kutta methods of first and second orders, while Runge-Kutta is fourh-order method. All the difference in these methods are in Butcher's table.
For example, if for Runge-Kutta Y(n+1) = Y(n) + 1/6 * ( K1 + 2K2 + 2K3 + K4), where K1 is used in Euler's method.
After that, when i understood how to solve, i started to programming.

### Coding part
First my researches on "how to create GUI in Java) bring me to Swing lib.(based on AWT). Idk why, but i decided that it is good and "modern" way. Never have i ever been so wrong. I spent to many hours forking with Swing, complainig about it any crying. After some time, when i already implemented my own Graph api via Swing, i found out that there is easiest way: JavaFX lib. Then i rebuilt the whole project in order to work with JavaFX. There was not so much problems with JavaFX except layaout and desing decisions. It was quite useful to work with JavaFX.

### Problems
Because of my impelemtation, i have one unpleasang issue.
The consequence of that is provided in global error grap. With some period, errors fall down. It was not easy to catch the reasong of this, but it is more harder to implement in that way where there will not be such a problem.
The key of this situaiong is that, for example, if we have inteval [0,100] and step h = 50, first poing will be at (0,y1), second at (50.00000002, y2) and the last one will not be proccessed because it will be (100.00000004,y3). It is because Double is represented as floating point number. and FPN has problems like that.
Because of that, with step h = 50 and inteval [0;100], we will have only 2 points polted. but in order to compute global error correctly, we have to process all points in interval. For example, if h = 24, the problem disappear. 
Also, it is important to mention that my global error is max local error with certain step.
After some time i fixed it.

### Analizing
Graphs of local error quite easy to understand -- the more you "go to the right" the more error you get. The most precise method is Runge-kutta according to graph of local errors.
Global error shows that from some step, the worst method "stabylize" and does not become less precise.

## Goals of this project.
- Earned experience in JavaFX and GUI at general.
- Earned a litle bit deeper knowlede of numerical methods.
- Earned undertanding of what is happening when you decrese/increase step.


## Graphs
## Solution graphs
![alt text](https://github.com/PRYRAN/DEAssigmnet/blob/master/Solutuin%20Graphs.jpg)
## Local Error graphs
![alt text](https://github.com/PRYRAN/DEAssigmnet/blob/master/Local%20Error%20Graphs.jpg)
## Global Error graphs
![alt text](https://github.com/PRYRAN/DEAssigmnet/blob/master/Global%20Error%20graph.jpg)





