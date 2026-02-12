# 🚂 Railway.app Deployment - Step by Step

Railway is the **easiest way** to deploy your Spring Boot app. Free tier available, automatic builds, one-click deployment.

## ✅ Why Railway?

- ✅ **Free tier** - Generous free credits
- ✅ **Auto-detect** - Recognizes Maven/Java projects
- ✅ **GitHub integration** - Push-to-deploy
- ✅ **Databases** - Add MySQL/PostgreSQL with one click
- ✅ **Environment variables** - Easy configuration
- ✅ **Logs** - Real-time viewing
- ✅ **Public URL** - Get live endpoint immediately

---

## 🚀 5-Minute Deployment

### Step 1: Create Railway Account

1. Go to [Railway.app](https://railway.app)
2. Click **"Start a New Project"**
3. Sign up with GitHub (recommended)
4. Authorize Railway to access your repositories

### Step 2: Create New Project

1. Click **"New Project"** (in Dashboard)
2. Select **"Deploy from GitHub repo"**
3. Find: `sri2200030896/payment-processing-mvp1`
4. Click **"Deploy Now"**

Railway will:
- ✅ Detect Java/Maven project
- ✅ Build with Maven
- ✅ Run tests
- ✅ Deploy as Docker container
- ✅ Assign public URL

### Step 3: Add MySQL Database (Optional)

If you want persistent data:

1. In Railway dashboard, click **"New"**
2. Select **"Database"** → **"MySQL"**
3. Railway creates database automatically
4. Note the connection details

### Step 4: Configure Environment Variables

1. Click **"Variables"** tab
2. Click **"RAW Editor"**
3. Paste these (modify as needed):

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://${{ database.MYSQLHOST }}:3306/${{ database.MYSQL_DATABASE }}
spring.datasource.username=${{ database.MYSQLUSER }}
spring.datasource.password=${{ database.MYSQLPASSWORD }}
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
server.port=8080
```

4. Click **"Deploy"**

### Step 5: Get Your Live URL

1. Go to **Deployments** tab
2. Look for green checkmark ✅
3. Find **Public URL** - something like:
   ```
   https://payment-processing-mvp-prod-XXXXX.railway.app
   ```
4. Click to visit your live app!

---

## 📊 Dashboard Overview

After deployment, you'll see:

```
┌─────────────────────────────────────────┐
│  Payment Processing MVP (Deployment)    │
├─────────────────────────────────────────┤
│  Status:     ✅ Active/Running          │
│  Region:     🌍 Selected Region         │
│  Memory:     512 MB / 1 GB               │
│  Uptime:     5 days, 2 hours            │
│                                         │
│  Public URL: https://...railway.app    │
│  Logs:       [View Real-time]           │
│  Variables:  [20 env vars set]          │
│  Domains:    [Add Custom Domain]        │
└─────────────────────────────────────────┘
```

---

## 🔑 Using Your Deployed App

### Access Payment Form
```
https://your-railway-url/
```

### Login to Dashboard
```
https://your-railway-url/login.html
Credentials: srikanth / 1234
```

### API Health Check
```bash
curl https://your-railway-url/api/health
```

### Create Payment
```bash
curl -X POST https://your-railway-url/api/payment \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "contact": "9876543210",
    "amount": "1500.00"
  }'
```

---

## 🔐 Email Configuration (SMTP)

To enable email confirmations:

### For Gmail:
1. Enable 2FA: https://myaccount.google.com/security
2. Create App Password: https://myaccount.google.com/apppasswords
3. Set environment variables:
   ```
   spring.mail.username=your_email@gmail.com
   spring.mail.password=xxxx xxxx xxxx xxxx
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   ```

### For Other Email Providers:
- **Outlook**: smtp-mail.outlook.com:587
- **SendGrid**: smtp.sendgrid.net:587
- **Mailgun**: smtp.mailgun.org:587

---

## 📈 Monitoring & Logs

### View Real-time Logs:
1. Dashboard → **Logs** tab
2. See all application output
3. Search by keywords
4. Download logs

### Common Log Messages:
```
✅ "Added connection conn0: url=jdbc:h2:mem:payment_db"
✅ "Tomcat started on port(s): 8080"
✅ "Payment Processing API is running"
```

### Monitor Metrics:
- CPU usage
- Memory usage
- Request count
- Error rate
- Response times

---

## 🔄 Auto-Deploy on Push

Once set up, **every push to `main` branch** auto-deploys:

```bash
# Make changes locally
git add .
git commit -m "Update payment form"

# Push to GitHub
git push origin main

# Railway automatically:
# 1. Pulls latest code
# 2. Builds with Maven
# 3. Runs tests
# 4. Deploys new version
# 5. Shows logs in real-time
```

Check deployment status: Dashboard → **Deployments** tab

---

## 💰 Pricing & Free Tier

**Railway Free Tier:**
- $5/month in credits
- Build: ✅ Unlimited
- Deployments: ✅ Unlimited
- Public URL: ✅ Included
- MySQL: ✅ Included (100MB)

**Example Monthly Cost:**
- Web service: $7/month
- MySQL: $5/month
- **Total: $12/month** (after $5 free credit)

Much cheaper than AWS/Heroku!

---

## 🚨 Troubleshooting

### "Build Failed"
- Check logs in Railway dashboard
- Usually: Java version or dependency issue
- Solution: Push fix to GitHub, Railway auto-rebuilds

### "Port Already in Use"
- Railway automatically assigns port 8080
- No need to configure

### "Database Connection Failed"
- Verify MySQL is connected
- Check environment variables are set
- Use Railway's auto-filled variables

### "Email Not Sending"
- Check SMTP credentials in environment variables
- Verify Gmail App Password (not regular password)
- Check spam folder

---

## 🎯 Next Steps

1. **Deployed?** ✅ Share your URL!
   ```
   https://your-railway-url/
   ```

2. **Add Custom Domain** (optional)
   - Railway → Domains → Add Domain
   - Point DNS to Railway
   - Get HTTPS automatically

3. **Monitor Performance**
   - Watch logs regularly
   - Check error rates
   - Monitor resource usage

4. **Backup Data**
   - Export MySQL regularly
   - Railway backup features
   - GitHub repository backup

---

## 📝 Quick Command Reference

```bash
# Test locally before deploying
mvn clean package

# Build Docker image
docker build -t payment-processing-mvp .

# Push code (triggers Railway auto-deploy)
git push origin main

# Check Railway status
# Go to: https://railway.app/dashboard

# View live logs
# Go to: Dashboard → Logs (real-time)
```

---

## ✨ You're Done!

Your Payment Processing MVP is now **live on the internet** 🎉

**Share your URL:**
- Portfolio website
- LinkedIn profile
- GitHub README
- Resume

**Example Portfolio Link:**
```markdown
## Payment Processing MVP
- **Live Demo:** https://payment-processing-mvp-prod-XXXXX.railway.app
- **GitHub:** https://github.com/sri2200030896/payment-processing-mvp1
- **Features:** UPI payments, Authentication, QR codes, Email
```

---

**Questions?** Check Railway docs: https://docs.railway.app
