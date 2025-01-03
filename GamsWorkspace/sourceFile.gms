Sets
    m   / m1*m2 /
    a   / a1*a5 /
    wd  / wd1*wd5/;

Parameter
    priority(a)
    expectedHours(a)
    maxWorkingHours(m)
    maxWorkingHoursWeek(m)
    dayWeight(wd);

dayWeight("wd1") = 1;
dayWeight("wd2") = 2;
dayWeight("wd3") = 3;
dayWeight("wd4") = 4;
dayWeight("wd5") = 5;

*1 = high, 2 = normal, 3 = less
priority("a1") = 2;
priority("a2") = 2;
priority("a3") = 1;
priority("a4") = 1;
priority("a5") = 3;

expectedHours("a1") = 2;
expectedHours("a2") = 9;
expectedHours("a3") = 3;
expectedHours("a4") = 5;
expectedHours("a5") = 4;

maxWorkingHours(m) = 10;
maxWorkingHoursWeek(m) = 40;

Variables
    obj;

Binary Variables
    x(m,a,wd);
    
Equations 
    mip;

*Zielfunktion    
mip .. obj =e= sum((m,a,wd), (1/(priority(a)+dayWeight(wd)))*x(m,a,wd));






Equations
    nb1
    nb2
    nb3;

*Nebenbedingungen

*Jeder Auftrag darf nur einem Mitarbeiter an einem Werktag zugeteilt werden,
*kann aber auch unzugewiesen bleiben.
nb1(a) .. sum((m,wd), x(m,a,wd)) =l= 1;

*Ein Mitarbeiter darf an einem Werktag seine maximalen Arbeitsstunden nicht überschreiten
nb2(m, wd) .. sum((a), expectedHours(a) * x(m,a,wd)) =l= maxWorkingHours(m);

*Ein Mitarbeiter darf in einer Werkwoche seine maximalen Arbeitsstunden nicht überschreiten
nb3(m) .. sum((a,wd), expectedHours(a) * x(m,a,wd)) =l= maxWorkingHoursWeek(m);


*Solve
option mip = CPLEX;
Model optModel /all/;
Solve optModel using mip maximize obj;


Display x.l;