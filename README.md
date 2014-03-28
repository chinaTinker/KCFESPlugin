KCFESPlugin
===========

KCF Indice update river plugin

This is a Elasticsearch river plugin that is used to update the indice instantly.

I have set a Looker tasker to watch newest record from db in loop with a delay. 
And I give a Updater whitch extends from Observer, to observe the looks.

Yes, it`s a observer-observable pattern.

This program can be modified easily, because I seprate the logic part and basic part.
If you want to do your job, simplly write you logical code!!

Enjoy yourself!!
