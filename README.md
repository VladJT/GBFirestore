# GBFirestore

Приложение, которое хранит и отображает давление и пульс пользователя по времени. В
приложении всего один экран, отображающий данные в списке, разделенным по дням и часам. По
нажатию на FAB вводятся новые данные. Все данные хранятся в Firestore.


Стек: MVVM, Flow, Coroutines, DAGGER2

В зависимости от показателей давления цвет меняется от зеленого к желтому и красному.
- const val GOOD_PRESSURE1 = 120
- const val MIN_PRESSURE1 = 100
- const val MAX_PRESSURE1 = 150
- const val GOOD_PRESSURE2 = 70
- const val MIN_PRESSURE2 = 65
- const val MAX_PRESSURE2 = 90


<img src="https://github.com/VladJT/GBFirestore/assets/95467816/c59cc7fd-1bb5-4e07-98cb-91905f0ee84f.png" width="250" >
<img src="https://github.com/VladJT/GBFirestore/assets/95467816/c209e887-6887-413e-a9a9-53ad2f93eb9b.png" width="250" >

Добавление заметки:

<img src="https://github.com/VladJT/GBFirestore/assets/95467816/33af41c6-0c9f-43db-a442-a9aa96ae07d1.png" width="250" >
