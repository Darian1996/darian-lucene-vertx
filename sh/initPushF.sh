git init
git remote add gitee git@gitee.com:Darian1996/darian-lucene-vertx.git
git remote add github git@github.com:Darian1996/darian-lucene-vertx.git
git add .
git commit -m "$(date "+%Y-%m-%d %H:%M:%S AutoCommit.sh by Darian")"
git push -f gitee master
git push -f github master