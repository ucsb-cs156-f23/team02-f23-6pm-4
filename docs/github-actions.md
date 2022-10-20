# Setting up GitHub Actions

To setup GitHub Actions so that the tests pass, you will need to configure
a few *secrets* on the GitHub repo settings page.

1. Navigate to the settings page for your repo on the GitHub web interface.
2. Click on Settings, and look in the left navigation for Secrets.  If you
   have a brand new repo, this page should show a message `There are no secrets for
   this repository.`
3. At the upper right hand corner, there is a "New Secret" button.  For each of
   the secrets in the table below, please use this button to create a new
   secret.

Here are the secrets you need to create:


| Secret Name | Value | 
|-|-|
| `DOCS_TOKEN` | This value is a personal access token that allows the Github Actions script to publish the Storybook for this repo to the separate GitHub Pages repos. To obtain a value for this token, please refer to the instructions below.  |

# Obtaining a personal access token `DOCS_TOKEN` for storybook

The GitHub actions script to deploy the Storybook to QA requires that a repository secret called `DOCS_TOKEN` be set up; this should be an access token for the repository. This secret can be obtained by visiting the settings page for your personal GitHub account, visiting Developer Settings, and then Personal Access Tokens. The page is also linked [here](https://github.com/settings/tokens).

![image](https://user-images.githubusercontent.com/1119017/147836507-0190801c-ce94-4e5a-9abe-6a1d2d0455af.png)

