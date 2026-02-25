# Module 8 - Build Automation & CI/CD with Jenkins

This repository contains a demo project created as part of my **DevOps studies** in the **TechWorld with Nana – DevOps Bootcamp**.

https://www.techworld-with-nana.com/devops-bootcamp

***Demo Project:*** Configure Webhook to trigger CI Pipeline automatically on every change

***Technologies used:*** Jenkins, GitHub, Git, Docker, Java, Maven

***Project Description:*** 

- Install GitHub Plugin in Jenkins
- Configure GitHub access token and connection to Jenkins in GitHub project settings
- Configure Jenkins to trigger the CI pipeline, whenever a change is pushed to GitHub

---

### Step 1 — Install GitHub Plugins in Jenkins

Verify the following plugins are installed before proceeding:

| Plugin | Purpose |
|---|---|
| `GitHub Plugin` | Core GitHub integration |
| `GitHub Integration` | Enables webhook-based triggers |
| `GitHub Branch Source Plugin` | Required for multibranch pipelines |

**How to check / install:**

1. Go to **Manage Jenkins** → **Plugins** → **Installed plugins**
2. Search for each plugin by name
3. If missing, switch to the **Available** tab, find the plugin, and install it

---

### Step 2 — Configure GitHub Access Token & Server Connection

#### 2a. Add a GitHub Server in Jenkins

1. Go to **Manage Jenkins** → **System** → scroll to the **GitHub** section
2. Click **Add GitHub Server** and fill in:

| Field | Value |
|---|---|
| Name | `GitHub` |
| API URL | `https://api.github.com` |

#### 2b. Create a GitHub Personal Access Token

1. Navigate to [https://github.com/settings/tokens/new](https://github.com/settings/tokens/new)
2. Set the **Note** to `jenkins`
3. Select the following scopes:

| Scope | Reason |
|---|---|
| `admin:repo_hook` | Create, read, and delete webhooks |
| `public_repo` | Access public repositories |
| `repo:status` | Update commit statuses |

> **Note:** Add the `repo` scope only if you need access to private repositories.

![GitHub Token Scopes](./images/github-token.png)

#### 2c. Add the Token as a Jenkins Credential

1. In the **GitHub Server** config, click **Add** next to Credentials → **Jenkins**
2. Choose **Secret text** and fill in:

| Field | Value |
|---|---|
| ID | `github-token` |
| Scope | `Global` |
| Secret | `<your generated token>` |

3. Click **Test connection** — you should see `Credentials verified`
4. Check **Manage hooks** so Jenkins can auto-create webhooks on GitHub
5. Click **Save**

![GitHub Server Configuration](./images/github-server.png)

---

### Step 3 — Trigger CI Pipeline on Push

#### 3a. Create the Jenkins Pipeline Job

1. Go to **Dashboard** → **New Item**
2. Name it `pipeline` and select **Pipeline**

**General tab:**
- Check `GitHub project`
- Repo URL: `https://github.com/explicit-logic/jenkins-module-8.4`

**Build Triggers tab:**
- Check `GitHub hook trigger for GITScm polling`

**Pipeline tab:**

| Field | Value |
|---|---|
| Definition | `Pipeline script from SCM` |
| SCM | `Git` |
| Repository URL | `https://github.com/explicit-logic/jenkins-module-8.4` |
| Credentials | `github` (GitHub username + password/token) |
| Branch | `*/pipeline` |
| Script Path | `Jenkinsfile` |

3. Click **Save**

#### 3b. Add a Webhook in GitHub

1. Navigate to your repo → **Settings** → **Webhooks** → **Add webhook**
2. Configure it as follows:

| Field | Value |
|---|---|
| Payload URL | `http://<DROPLET_IP>:8080/github-webhook/` |
| SSL verification | Disabled |
| Events | `Just the push event` |

3. Click **Add webhook**

![Webhook Setup](./images/webhook.png)

![CI Pipeline Demo](./images/pipeline-demo.gif)

---

### Step 4 — Configure a Multibranch Pipeline

A multibranch pipeline automatically discovers branches and pull requests, creating a pipeline job for each.

#### 4a. Create the Multibranch Pipeline Job

1. Go to **Dashboard** → **New Item**
2. Name it `multibranch`, select **Multibranch Pipeline**, click **OK**

**Branch Sources:**
- Click **Add source** → **GitHub**

| Field | Value |
|---|---|
| Credentials | `github` |
| Repository HTTPS URL | `https://github.com/explicit-logic/jenkins-module-8.4` |

- Click **Validate** to confirm access

**Behaviors** — click **Add** and include:
- `Discover branches`
- `Discover pull requests from origin`

**Build Configuration:**
- Script Path: `Jenkinsfile`

**Scan Multibranch Pipeline Triggers:**
- Check **Periodically if not otherwise run** → set interval to `1 day`

3. Click **Save** — Jenkins will scan the repository and create jobs for each branch

![Multibranch Pipeline Demo](./images/multibranch-demo.gif)
