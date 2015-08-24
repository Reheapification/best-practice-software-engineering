Only use rebase wen there are no or few merge conflicts. Otherwise do a merge.
See [this post on stackoverflow.com](http://stackoverflow.com/questions/539062/what-are-your-experiences-using-the-new-hg-rebase-command)

# don't do a merge #
```
hg incoming
hg pull
hg merge
hg stat
hg commit
hg push
```
# instead do a rebase #
```
hg incoming
hg pull --rebase
hg stat
hg commit
hg push
```
or
```
hg incoming
hg pull
hg rebase
hg stat
hg commit
hg push
```


# activating the rebase option #

.hgrc:
```
[extensions]
hgext.convert=
hgext.graphlog =
hgext.mq =
hgext.purge =
hgext.rebase =
```