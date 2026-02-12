# 🚀 Deployment Guide - Payment Processing MVP

This guide covers deploying your Spring Boot application to various platforms using GitHub Actions and Docker.

## 📋 Table of Contents
1. [Quick Start - Railway.app (Recommended)](#quick-start---railwayapp)
2. [GitHub Actions Setup](#github-actions-setup)
3. [Docker Deployment](#docker-deployment)
4. [Other Platforms](#other-platforms)
5. [Environment Variables](#environment-variables)

---

## Quick Start - Railway.app (Recommended ⭐)

Railway.app is the easiest option - free tier available, GitHub integration, automatic deployments.

### Step 1: Connect Railway to GitHub

1. Go to [Railway.app](https://railway.app)
2. Click **"New Project"** → **"Deploy from GitHub repo"**
3. Select your repository: `sri2200030896/payment-processing-mvp1`
4. Railway automatically detects Maven project and builds it

### Step 2: Configure Environment Variables

In Railway dashboard → Variables:

```
spring.datasource.url=jdbc:mysql://your-mysql-host/payment_db
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

### Step 3: Deploy

Click **"Deploy"** - Railway automatically:
- ✅ Detects Maven project
- ✅ Builds with Java 22
- ✅ Runs tests
- ✅ Deploys as Docker container
- ✅ Provides public URL

Your app will be live at: `https://payment-processing-mvp-prod-XXXXX.railway.app`

---

## GitHub Actions Setup

Your repository now has two automated workflows:

### 1. **build-and-test.yml** (Automatic on every push)
- Builds Maven project with Java 22
- Runs all tests
- Creates Docker image
- Uploads JAR artifact

### 2. **docker-push.yml** (Push to Docker Registry)
To enable Docker image push, set these secrets:

**GitHub Settings → Secrets and variables → Actions:**

```
DOCKER_USERNAME = your_dockerhub_username
DOCKER_PASSWORD = your_dockerhub_token
```

Get Docker Hub token:
1. Go to [hub.docker.com](https://hub.docker.com)
2. Login → Account Settings → Security → New Access Token
3. Copy and save in GitHub Secrets

---

## Docker Deployment

### Option A: Docker Hub + Render.com

**Step 1: Push to Docker Hub**

```bash
# Login to Docker locally
docker login

# Build image
docker build -t yourusername/payment-processing-mvp:latest .

# Push to Docker Hub
docker push yourusername/payment-processing-mvp:latest
```

**Step 2: Deploy on Render.com**

1. Go to [Render.com](https://render.com)
2. Click **"New +"** → **"Web Service"**
3. Choose **"Public image registry"**
4. Image URL: `yourusername/payment-processing-mvp:latest`
5. Port: `8080`
6. Add environment variables
7. Click **"Create Web Service"**

Your app will be live at a Render URL.

### Option B: Local Docker Run

```bash
# Build image
docker build -t payment-processing-mvp:latest .

# Run container
docker run -p 8080:8080 \
  -e spring.datasource.url=jdbc:mysql://host.docker.internal:3306/payment_db \
  -e spring.datasource.username=root \
  -e spring.datasource.password=password \
  payment-processing-mvp:latest
```

---

## Other Platforms

### Fly.io (Free tier, good performance)

```bash
# Install flyctl
brew install flyctl

# Login
flyctl auth login

# Initialize
cd /Users/vijaykadarla/Desktop/payment-processing-mvp
flyctl launch

# Deploy
flyctl deploy

# View logs
flyctl logs
```

### Heroku (Paid, but reliable)

```bash
# Install Heroku CLI
brew tap heroku/brew && brew install heroku

# Login
heroku login

# Create app
heroku create payment-processing-mvp

# Deploy
git push heroku main

# View logs
heroku logs --tail
```

### AWS Elastic Beanstalk

1. Install AWS CLI: `brew install awscli`
2. Install EB CLI: `pip install awsebcli`
3. Run: `eb init -p "Java 22 running on 64bit Amazon Linux 2"`
4. Deploy: `eb create payment-processing-mvp`
5. Check: `eb status`

---

## Environment Variables

### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/payment_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### Email Configuration
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Optional Server Configuration
```properties
server.port=8080
spring.application.name=payment-processing-mvp
logging.level.root=INFO
logging.level.com.payment=DEBUG
```

---

## GitHub Actions Status

Check your workflow status:

1. Go to: `https://github.com/sri2200030896/payment-processing-mvp1/actions`
2. View build logs, test results, and artifacts
3. See Docker image build status

---

## Monitoring Deployments

### Railway.app
- Dashboard shows real-time logs
- Automatic rollback on failed deploy
- View metrics and resource usage

### Render.com
- Deployment history
- Log viewer
- Manual/automatic deploys

### GitHub Actions
- Workflow run details
- Build artifacts
- Deployment history

---

## Troubleshooting

**Build fails with Java version error**
- Platform uses Java 22 ✅ (configured in workflow)

**Port 8080 already in use**
- Docker forwards port: `-p 8080:8080`
- Change port in application.properties

**Database connection fails**
- Verify database is running
- Check credentials in environment variables
- Use connection string format: `jdbc:mysql://host:3306/db`

**Docker image too large**
- Multi-stage build reduces size (configured in Dockerfile)
- Uses slim JDK image for runtime

---

## Next Steps

### Recommended Deployment Path:
1. ✅ Set up GitHub Actions (done!)
2. → Deploy to Railway.app (easiest)
3. → Connect MySQL database
4. → Configure email (SMTP)
5. → Share live URL

### To Get Live URL:
```bash
# After deploying to Railway/Render/Fly
# Your app is available at the platform's URL
# Share it on portfolio, LinkedIn, GitHub
```

---

## Quick Reference

| Platform | Effort | Cost | Best For |
|----------|--------|------|----------|
| **Railway** | ⭐ Easiest | Free tier | Beginners |
| **Render** | ⭐⭐ Easy | Free tier | Growing apps |
| **Fly.io** | ⭐⭐ Easy | Free tier | Reliable hosting |
| **Heroku** | ⭐⭐⭐ Medium | Paid | Enterprise |
| **AWS** | ⭐⭐⭐⭐ Hard | Paid | Scalable |

**Recommendation: Start with Railway.app for immediate deployment!**

---

For detailed setup instructions on your chosen platform, run:
```bash
cat DEPLOYMENT_DETAILED.md
```
