# captsone-nf-ordersystem
Welcome the capstone project. This program can be used as a web application for a order system in a company.
## Define variable:
In backend/src/main/resources/application.properties the environment variable MONGO_DB_CONNECTION_STRING needs to be defined. Set it as a connection string to a mongoDB of your choice.
## Login needs a User
Once you have established the connection to your MongoDB. Open your MongoDB manually with for example MongoCompas. Create a Database named CapstoneOrderSystemDB and a Collection UserList. Now import the UserList.json. Afterwards you can use 3 test Accounts with different authorities. Each user has the password '1234'.
Note that this has been updated in PR LimitAddOrderByAuthority.
## Adding a example product list:
If one wants to use an examplary product list the UserList.json can be imported in the CapstoneOrderSystemDB as a collection under the nae ProductList.
## Create Users (Only with users testLead)
With the authority lead it is possible to create new users giving the tools inside the app.
## Product List in Hub (Only with user testPurchase):
You need to add products to your mongodb before you can see any products. This can be done with the add product feature on the website. Note that a user can only add products to his Orders with matching authorities.
## Order List in Hub:
After you added products to your product list you can add orders. Afterwards they are displayed in the order hub.
## Approve & Disapprove orders (Only with users testLead & testPurchase):
If you have authority lead or purchase you can approve or disapprove orders. You can only approve already existing orders. When both a lead and a purchase have approved the order it's status will change to approved. Only one disapprove is needed to reject an order.
