Sets
    m   / m1*m2 /
    a   / a1*a15 /
    wd  / wd1*wd5/
    s   / s1*s5/
    index /i1*i15/;

Alias(a, aa);

Parameter
    priority(a)
    expectedHours(a)
    maxWorkingHours(m)
    maxWorkingHoursWeek(m)
    minWorkingHours(m)
    dayWeight(wd) 
    employeeSkill(m,s)
    necessarySkill(a,s)
    driveTime(a,aa);

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
expectedHours("a2") = 1;
expectedHours("a3") = 3;
expectedHours("a4") = 2;
expectedHours("a5") = 4;
expectedHours("a6") = 1;
expectedHours("a7") = 4;
expectedHours("a8") = 1;
expectedHours("a9") = 8;
expectedHours("a10") = 9;
expectedHours("a11") = 2;
expectedHours("a12") = 9;
expectedHours("a13") = 4;
expectedHours("a14") = 1;
expectedHours("a15") = 2;

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


Sets
 location1(a) /a1*a5/
 location2(a) /a6*a10/
 location3(a) /a11*a15/;

driveTime(a, aa) = 0.1;

driveTime(location1, location2) = 0.7;
driveTime(location2, location1) = 0.7;

driveTime(location2, location3) = 0.9;
driveTime(location3, location2) = 0.9;

driveTime(location1, location3) = 4;
driveTime(location3, location1) = 4;

loop(a,
    driveTime(a, a) = 0;
);








maxWorkingHours(m) = 10;
maxWorkingHoursWeek(m) = 40;
minWorkingHours(m) = 2;

*1 wenn benutzer den Skill besitzt sonst 0
employeeSkill("m1","s1") = 1;
employeeSkill("m1","s2") = 1;
employeeSkill("m2","s2") = 1;
employeeSkill("m2","s3") = 1;
employeeSkill("m2","s4") = 1;

Variables
    obj;

Binary Variables
    x(m,a,wd,index)
    both_contracts_consecutively(m,a,aa,wd, index);
    
Equations 
    mip;

*Zielfunktion
mip .. obj =e= 
    sum((m,a,aa,wd,index)$(not ord(a) = ord(aa)),
              ((1 / (priority(a) + dayWeight(wd))) * x(m,a,wd,index))
            + ((1 / (driveTime(a,aa) + 0.1)) * both_contracts_consecutively(m,a,aa,wd,index))
    );





*Nebenbedingungen
Equations
    nb1
    nb2
    nb3
    nb4
    nb5
    nb6;
    
Equations
    nb_both_contracts_consecutively_1
    nb_both_contracts_consecutively_2
    nb_both_contracts_consecutively_3
    nb_both_contracts_consecutively_4
    nb_no_index_jumps;
    

nb_both_contracts_consecutively_1(m,a,aa,wd,index)$(ord(index) < card(index)) .. both_contracts_consecutively(m,a,aa,wd,index) =l= x(m,a,wd,index);
nb_both_contracts_consecutively_2(m,a,aa,wd,index)$(ord(index) < card(index)) .. both_contracts_consecutively(m,a,aa,wd,index) =l= x(m,aa,wd,index+1);
nb_both_contracts_consecutively_3(m,a,aa,wd,index)$(ord(index) < card(index)) .. both_contracts_consecutively(m,a,aa,wd,index) =g= x(m,a,wd,index) + x(m,aa,wd,index+1) - 1;
nb_both_contracts_consecutively_4(m,a,aa,wd,index)$(ord(index) = card(index)) .. both_contracts_consecutively(m,a,aa,wd,index) =e= 0;
nb_no_index_jumps(m,wd,index)$(ord(index) > 1) .. sum(aa, x(m,aa,wd,index)) =l= sum(a, x(m,a,wd,index - 1));


*nb1: Jeder Auftrag darf nur einem Mitarbeiter an einem Werktag zugeteilt werden,
*kann aber auch unzugewiesen bleiben.
nb1(a) .. sum((m,wd,index), x(m,a,wd,index)) =l= 1;

*nb2: Ein Mitarbeiter darf an einem Werktag seine maximalen Arbeitsstunden nicht überschreiten
nb2(m, wd) .. sum((a,index), expectedHours(a) * x(m,a,wd,index)) + sum((a,aa,index), driveTime(a,aa) * both_contracts_consecutively(m,a,aa,wd,index)) =l= maxWorkingHours(m);

*nb3: Ein Mitarbeiter darf in einer Werkwoche seine maximalen Arbeitsstunden nicht überschreiten
nb3(m) .. sum((a,wd,index), expectedHours(a) * x(m,a,wd,index)) + sum((a,aa,wd,index), driveTime(a,aa) * both_contracts_consecutively(m,a,aa,wd,index)) =l= maxWorkingHoursWeek(m);

*nb4: Ein Mitarbeiter muss alle nötigen Skills besitzen um einem Auftrag zugewiesen werden zu können
Parameter
    noSkillNecessary(a);
noSkillNecessary(a) = no$(sum(s, necessarySkill(a,s)) >= 1);
nb4(m,a,wd,index) .. (prod((s)$(necessarySkill(a,s)), employeeSkill(m,s))) + noSkillNecessary(a) =g= x(m,a,wd,index);

*nb5: Ein Mitarbeiter muss an einem Werktag seine mindest Arbeitsstunden leisten
nb5(m, wd) ..  sum((a,index), expectedHours(a) * x(m,a,wd,index)) + sum((a,aa,index), driveTime(a,aa) * both_contracts_consecutively(m,a,aa,wd,index)) =g= minWorkingHours(m);

* nb6: Ein Mitarbeiter darf an einem Wochentag nur einmal einen bestimmten Index verwenden
nb6(m, wd, index) .. sum((a), x(m,a,wd,index)) =l= 1;










Option solver = CPLEX
Model optModel /all/;
Solve optModel using mip maximize obj;


Display x.l;
Display both_contracts_consecutively.l;
Display nb2.l;
Display nb3.l;
Display nb5.l;
Display driveTime;