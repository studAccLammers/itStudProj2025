Sets
    m   / m1*m2 /
    a   / a1*a5 /
    wd  / wd1*wd5/
    s   / s1*s5/;

Parameter
    priority(a)
    expectedHours(a)
    maxWorkingHours(m)
    maxWorkingHoursWeek(m)
    dayWeight(wd) 
    employeeSkill(m,s)
    necessarySkill(a,s); 

*Freitage werden in der Berechnung höher bestraft als Montage
dayWeight("wd1") = 1;
dayWeight("wd2") = 2;
dayWeight("wd3") = 3;
dayWeight("wd4") = 4;
dayWeight("wd5") = 5;

*1 = Höchste, 2=Hoch, 3= Normal
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

*1 wenn benutzer den Skill besitzt sonst 0
employeeSkill("m1","s1") = 1;
employeeSkill("m1","s2") = 1;
employeeSkill("m2","s2") = 1;
employeeSkill("m2","s3") = 1;
employeeSkill("m2","s4") = 1;

*1 wenn Auftrag den Skill benötigt sonst 0
necessarySkill("a1","s2") = 1;
necessarySkill("a1","s3") = 1;
necessarySkill("a2","s1") = 1;
necessarySkill("a3","s4") = 1;

Variables
    obj;

Binary Variables
    x(m,a,wd);
    
Equations 
    mip;

*Zielfunktion
mip .. obj =e= sum((m,a,wd), (1/(priority(a)+dayWeight(wd)))*x(m,a,wd));





*Nebenbedingungen
Equations
    nb1
    nb2
    nb3
    nb4;

*Jeder Auftrag darf nur einem Mitarbeiter an einem Werktag zugeteilt werden,
*kann aber auch unzugewiesen bleiben.
nb1(a) .. sum((m,wd), x(m,a,wd)) =l= 1;

*Ein Mitarbeiter darf an einem Werktag seine maximalen Arbeitsstunden nicht überschreiten
nb2(m, wd) .. sum(a, expectedHours(a) * x(m,a,wd)) =l= maxWorkingHours(m);

*Ein Mitarbeiter darf in einer Werkwoche seine maximalen Arbeitsstunden nicht überschreiten
nb3(m) .. sum((a,wd), expectedHours(a) * x(m,a,wd)) =l= maxWorkingHoursWeek(m);

*Ein Mitarbeiter muss alle nötigen Skills besitzen um einem Auftrag zugewiesen werden zu können
nb4(m,a,wd) .. prod(s, 1 - necessarySkill(a,s) + employeeSkill(m,s)) =g= x(m,a,wd);






*Solve
option mip = CPLEX;
Model optModel /all/;
Solve optModel using mip maximize obj;


Display x.l;