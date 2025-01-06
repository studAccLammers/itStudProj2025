Sets
    m   / m1*m2 /
    a   / a1*a15 /
    wd  / wd1*wd5/
    s   / s1*s5/;

Parameter
    priority(a)
    expectedHours(a)
    maxWorkingHours(m)
    maxWorkingHoursWeek(m)
    minWorkingHours(m)
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
priority("a6") = 2;
priority("a7") = 3;
priority("a8") = 1;
priority("a9") = 3;
priority("a10") = 2;
priority("a11") = 2;
priority("a12") = 1;
priority("a13") = 2;
priority("a14") = 3;
priority("a15") = 2;

expectedHours("a1") = 2;
expectedHours("a2") = 9;
expectedHours("a3") = 3;
expectedHours("a4") = 5;
expectedHours("a5") = 4;
expectedHours("a6") = 7;
expectedHours("a7") = 6;
expectedHours("a8") = 5;
expectedHours("a9") = 8;
expectedHours("a10") = 9;
expectedHours("a11") = 6;
expectedHours("a12") = 9;
expectedHours("a13") = 5;
expectedHours("a14") = 7;
expectedHours("a15") = 6;

*1 wenn Auftrag den Skill benötigt sonst 0
necessarySkill("a1","s2") = 1;
necessarySkill("a1","s3") = 1;
necessarySkill("a2","s1") = 1;
necessarySkill("a3","s4") = 1;
necessarySkill("a4","s2") = 1;
necessarySkill("a7","s3") = 1;
necessarySkill("a8","s4") = 1;
necessarySkill("a9","s2") = 1;
necessarySkill("a10","s5") = 1;
necessarySkill("a11","s1") = 1;
necessarySkill("a13","s4") = 1;
necessarySkill("a14","s2") = 1;
necessarySkill("a15","s5") = 1;

maxWorkingHours(m) = 10;
maxWorkingHoursWeek(m) = 40;
minWorkingHours(m) = 6;

*1 wenn benutzer den Skill besitzt sonst 0
employeeSkill("m1","s1") = 1;
employeeSkill("m1","s2") = 1;
employeeSkill("m2","s2") = 1;
employeeSkill("m2","s3") = 1;
employeeSkill("m2","s4") = 1;

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
    nb4
    nb5;

*nb1: Jeder Auftrag darf nur einem Mitarbeiter an einem Werktag zugeteilt werden,
*kann aber auch unzugewiesen bleiben.
nb1(a) .. sum((m,wd), x(m,a,wd)) =l= 1;

*nb2: Ein Mitarbeiter darf an einem Werktag seine maximalen Arbeitsstunden nicht überschreiten
nb2(m, wd) .. sum(a, expectedHours(a) * x(m,a,wd)) =l= maxWorkingHours(m);

*nb3: Ein Mitarbeiter darf in einer Werkwoche seine maximalen Arbeitsstunden nicht überschreiten
nb3(m) .. sum((a,wd), expectedHours(a) * x(m,a,wd)) =l= maxWorkingHoursWeek(m);

*nb4: Ein Mitarbeiter muss alle nötigen Skills besitzen um einem Auftrag zugewiesen werden zu können
Parameter
    noSkillNecessary(a);

noSkillNecessary(a) = no$(sum(s, necessarySkill(a,s)) >= 1);

nb4(m,a,wd) .. (prod((s)$(necessarySkill(a,s)), employeeSkill(m,s))) + noSkillNecessary(a) =g= x(m,a,wd);

*nb5: Ein Mitarbeiter muss an einem Werktag seine mindest Arbeitsstunden leisten
nb5(m, wd) .. sum(a, expectedHours(a) * x(m,a,wd)) =g= minWorkingHours(m);






*Solve
option mip = CPLEX;
Model optModel /all/;
Solve optModel using mip maximize obj;


Display x.l;