# revolut_transfer

application implements transfer logic from one account to another.

There are several preinstalled "Accounts" 


application is registered on 8888 port

api:

POST localhost:8888/transfer/ - commit transfer

{ moneyAmount: '100', debitAccountNumber: '10101010', creditAccountNumber: '10101020' }

In project you can useful test script for testing api and text file with examples of request

P.S. DI in application is extremly simple because of small count of classes so I decided to construct all dependencies in one module

AccountRepository works without locking. Optimistic approaches are used.
