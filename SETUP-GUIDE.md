# Tekton + ArgoCD Setup Guide (form4-graphql, staging test)

Yeh guide diagram wale flow ko banata hai. Sab kuch **safe** hai —
naya namespace `form4-test`, existing kuch nahi tootega.

---

## Files ka structure

```
tekton/
  01-task-git-clone.yaml         # code clone
  02-task-maven-build.yaml       # deps + build
  03-task-sonarqube-scan.yaml    # scan (optional, abhi skip)
  04-task-podman-build-push.yaml # image build + push
  05-task-update-gitops.yaml     # values.yaml me tag update + push
  06-pipeline.yaml               # sab tasks jodta hai
  07-pipelinerun.yaml            # pipeline MANUALLY start karta hai
argocd/
  01-application.yaml            # ArgoCD app (auto-sync)
```

---

## STEP 0 — Pehle yeh check karo (safety)

```bash
# ArgoCD applications abhi khaali hone chahiye (humne dekha tha)
kubectl get applications -n argocd

# Tekton chal raha hai confirm karo
kubectl get pods -n tekton-pipelines
```

---

## STEP 1 — Do secrets banao (git push + registry pull)

### (a) Git auth secret — Tekton ko GitOps repo me push karne ke liye
GitHub personal access token (PAT) chahiye hoga (repo write access).

```bash
kubectl create secret generic gitops-git-auth \
  -n tekton-pipelines \
  --from-literal=.git-credentials="https://<USERNAME>:<GITHUB_TOKEN>@github.com" \
  --type=Opaque
```

> Note: agar yeh format kaam na kare to Tekton ka basic-auth secret
> format use karenge — bata dena, adjust kar denge.

### (b) Registry pull secret — form4-test namespace me image pull ke liye
```bash
kubectl create namespace form4-test

kubectl create secret docker-registry nexus-pull-secret \
  -n form4-test \
  --docker-server=10.212.108.81:6001 \
  --docker-username=<REGISTRY_USER> \
  --docker-password=<REGISTRY_PASS>
```

> chart me `imagePullSecrets: nexus-pull-secret` likha hai, isliye
> yahi naam rakha hai.

---

## STEP 2 — Tekton Tasks + Pipeline apply karo

```bash
kubectl apply -f tekton/01-task-git-clone.yaml
kubectl apply -f tekton/02-task-maven-build.yaml
kubectl apply -f tekton/03-task-sonarqube-scan.yaml
kubectl apply -f tekton/04-task-podman-build-push.yaml
kubectl apply -f tekton/05-task-update-gitops.yaml
kubectl apply -f tekton/06-pipeline.yaml
```

Yeh sab sirf "definitions" hain — inse kuch chalta nahi, kuch tootata nahi.

---

## STEP 3 — ArgoCD Application apply karo

```bash
kubectl apply -f argocd/01-application.yaml
```

Ab ArgoCD GitOps repo watch karega. Abhi image tag purana hai, to
woh us tag ko deploy karne ki koshish karega. Agar woh image registry
me nahi hai, pod pending rahega — yeh normal hai, Tekton run ke baad
naya tag aayega.

---

## STEP 4 — Pipeline MANUALLY chalao (image banane ke liye)

`07-pipelinerun.yaml` me `tag` value badlo (jaise `test-001`), phir:

```bash
kubectl create -f tekton/07-pipelinerun.yaml
```

> `create` use karo, `apply` nahi (generateName har baar naya run banata hai)

Dekhne ke liye:
```bash
# saare runs
kubectl get pipelineruns -n tekton-pipelines

# live logs (tkn CLI ho to)
tkn pipelinerun logs -f -n tekton-pipelines --last
```

---

## STEP 5 — Deploy confirm karo

Pipeline ke aakhri step me GitOps repo me naya tag push hoga.
ArgoCD ~1-3 min me detect karke `form4-test` me deploy karega.

```bash
# ArgoCD app status
kubectl get application form4-graphql-test -n argocd

# pod chal raha hai?
kubectl get pods -n form4-test

# test - port forward karke check
kubectl port-forward -n form4-test svc/form4-graphql-svc 8080:8080
# phir browser/curl: http://localhost:8080
```

---

## Cleanup (test khatam hone pe — sab safe hat jayega)

```bash
kubectl delete -f argocd/01-application.yaml
kubectl delete namespace form4-test
# tekton definitions bhi hatana ho to:
kubectl delete -f tekton/
```

---

## Zaroori NOTES

1. **Image registry override**: chart me abhi `192.168.49.2:30082`
   (minikube) hai. Humne ArgoCD Application me isse
   `10.212.108.81:6001/ora` pe override kiya hai (parameters me).
   Behtar hoga chart ki values.yaml khud update kar do permanently.

2. **Base images**: tasks me maine `10.212.108.81:6001/ora/...` se
   maven, podman, git images maani hain. Agar yeh naam registry me
   alag hain to task files me image line update karni hogi.

3. **SonarQube**: abhi skip hai (URL khaali). Server + token milne pe
   PipelineRun me bhar dena.

4. **RBAC boundary**: Tekton cluster ko deploy nahi karta — sirf git
   me tag likhta hai. ArgoCD deploy karta hai. Yeh secure design hai.
```
