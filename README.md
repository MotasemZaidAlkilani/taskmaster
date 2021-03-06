
# taskmaster

## Overview
This is an application used to mange the tasks.

____________
### Lab 26

#### Feature Tasks

##### Homepage
The main page should have a heading at the top of the page, an image to mock the “my tasks” view,
and buttons at the bottom of the page to allow going to the “add tasks” and “all tasks” page.

![image description](screenshots/home.jpg)

_________

### Lab 27

#### Task Detail Page
Create a Task Detail page. It should have a title at the top of the page, and a Lorem Ipsum description.

![image description](screenshots/lab26Button.jpg)

![image description](screenshots/lab27Button.jpg)

![image description](screenshots/lab28Button.jpg)

____________

### Lab 28

#### Home Page

Refactor your homepage to use a RecyclerView for displaying Task data. This should have hardcoded Task data for now.
 you can tap on any one of the Tasks in the RecyclerView, and it will appropriately launch the detail page with the correct Task 
 title displayed.

#### Task Model

Create a Task class. A Task should have a title, a body, and a state. The state should be one of “new”, “assigned”, “in progress”, or “complete”

![image description](screenshots/homePageWithRecycleView.jpg)

___________

### Lab 29

#### Add Task Form

Modify your Add Task form to save the data entered in as a Task in your local database.

#### Homepage
Refactor your homepage’s RecyclerView to display all Task entities in your database.


![image description](screenshots/HomePageWithDatabaseLocalStorgae.jpg)

![image description](screenshots/deatailPageDatabaseLocalStorage.jpg)

__________

### Lab 31

#### Espresso Testing

Add more Espresso UI tests to your application

#### Polish
Complete / fix up / polish any remaining feature tasks from previous days’ labs.


![image description](screenshots/homePageChangeStates.jpg)

![image description](screenshots/taskDetailCompleteButton.jpg)

![image description](screenshots/completedTasks.jpg)
________
### Lab 32

#### Add Task Form
Modify your Add Task form to save the data entered in as a Task to DynamoDB.

#### Homepage
Refactor your homepage’s RecyclerView to display all Task entities in DynamoDB.

______
### Lab 33

#### Add Task Form
Modify your Add Task form to include either a Spinner or Radio Buttons for which team that task belongs to.

#### Settings Page
In addition to a username, allow the user to choose their team on the Settings page. Use that Team to display only that team’s tasks on the homepage.

![image description](screenshots/spinnerInAddTask.jpg)

![image description](screenshots/spinnerInSetting.jpg)

_________
### Lab 34 

#### App Polish
Ensure that your application follows Google’s guidelines. Make sure to get rid of any warnings in your app.

#### Build Final AAB
Build a signed AAB for your application. Include that AAB in your GitHub repo.

______________
### Lab 36

#### User Login
Add Cognito to your Amplify setup. Add in user login and sign up flows to your application, using Cognito’s pre-built UI as appropriate. Display the logged-in user’s username (or nickname) somewhere relevant in your app.

#### User Logout
Allow users to log out of your application.

_________
### Lab 37

#### Uploads
On the “Add a Task” activity, allow users to optionally select an image to attach to that task. If a user attaches an image to a task, that image should be uploaded to S3, and associated with that task.

#### Displaying Files
On the Task detail activity, if there is a file that is an image associated with a particular Task, that image should be displayed within that activity.

__________
### Lab 38

#### Adding a Task from Another Application
Add an intent filter to your application such that a user can hit the “share” button on an image in another application, choose TaskMaster as the app to share that image with, and be taken directly to the Add a Task activity with that image pre-selected.

______
### lab 39

#### Location
When the user adds a task, their location should be retrieved and included as part of the saved Task.

#### Displaying Location
On the Task Detail activity, the location of a Task should be displayed if it exists.

______________
### lab 41

#### Analytics
On the “Main” activity (and any other activities you like), start recording at least one AnalyticsEvent. Make sure you can view instances of that event, including their custom properties, in Amazon Pinpoint.

#### Text To Speech
On the Task Detail activity, add a button to read out the task’s description using the Amplify Predictions library.

#### Second Predictions Integration
Somewhere in your application, integrate one of the other Amplify Predictions APIs. See the possibilities at https://docs.amplify.aws/lib/predictions/getting-started/q/platform/android/#next-steps .

![image description](screenshots/speechAndTranslate.png)
__________
### lab 42

#### Banner Ad
On the “Main” activity, add a banner ad to the bottom of the page and display a Google test ad there.

#### Interstitial Ad
Add a button to the “Main” activity that allows users to see an interstitial ad. (In a real app, you’ll want to show this during natural transitions/pauses in your app, but we don’t really have that in this application.)

#### Rewarded Ad
Add a button to the “Main” activity that allows users to see a rewarded ad using a Google test ad. When the user clicks the close button, the user should see their reward in a text field next to the button.

![image description](screenshots/mainActivityWithAdsButtonsjpg.jpg)

______
