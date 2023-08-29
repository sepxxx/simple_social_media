# Social Media Application
>__Details Web messenger clone__ 
>You can subscribe, have friends, send messages, add posts and etc

## Used technologies
* **Java** 
* Spring Boot, Spring Data JPA(to work with entities),Sprint Security 
* **PostgreSQL**(main DBMS for this project)
* Maven(package manager to manipulate with dependecies)
* JWT Token(JSON Web Token for authorization and authentication)
* Docker(for run appllication in container)

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/sepxxx/simple_social_media
```
**2. Install Docker**

**3. Run the app using docker-compose**

```bash
docker-compose up
```
The app will start running at <http://localhost:8080>

## Explore Rest APIs

The app defines following CRUD APIs.

### auth-and-reg-controller
| Method | Url | Decription | Sample Valid Request Type | 
| ------ | --- | ---------- | --------------------------- |
| POST   | /reg | Sign up | UserRegistrationRequest |
| POST   | /auth | Log in | JwtRequest |

### user-controller
| Method | Url | Decription | Sample Valid Request Type | 
| ------ | --- | ---------- | --------------------------- |
| GET    | /users | List of all users(for admin) |  |
| GET    | /users/{id}| Get user by id | |
| DELETE| /users/{id}| Delete user by id|
| GET | /users/{id}/posts | Get user posts list by id | |

### conversation-controller
| Method | Url | Decription | Sample Valid Request Type | 
| ------ | --- | ---------- | --------------------------- |
| GET | /conversations |Get current user list of conversations| |
| GET | /conversations/{id}/messages |Get conversation messages| |
| POST| /conversations/messages/send  | Send message | MessageRequest|


### post-controller
| Method | Url | Decription | Sample Valid Request Type | 
| ------ | --- | ---------- | --------------------------- |
| POST| /posts/new | Add new post| Post Request|
| DELETE | /posts/{id} |Delete post by id| |
| GET | /posts/{id} | Get post by id| |
| PUT | /posts/{id} | Update post by id| |


### friends-and-subs-controller
| Method | Url | Decription | Sample Valid Request Type | 
| ------ | --- | ---------- | --------------------------- |
| GET | /users/{id}/subscribers | Get user subscribers |  |
| GET | /users/{id}/subscriptions | Get user subscriptions |  |
| GET | /users/{id}/friends | Get user friends |  |
| GET | /users/me/friendRequests | Get current user friend requests |  |
| POST | /users/{id}/subscribers | Subscribe user by id |  |
| DELETE | /users/{id}/subscribers | Unsubscribe user by id |  |


### activity-feed-controller
| Method | Url | Decription | Sample Valid Request Type | 
| ------ | --- | ---------- | --------------------------- |
| GET | /feed| Get current user activity feed |  |
