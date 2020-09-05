#!/bin/bash
declare -a VERSIONS=(
  1.16.0
  1.15.0
  1.14.0
  1.13.0
)
for i in "${VERSIONS[@]}"; do
  git checkout master
  RELEASE_BRANCH="release/$i"
  BRANCH="feature/$i_$1"
  git checkout -b $BRANCH $RELEASE_BRANCH
  git fetch origin
  git reset --hard origin/master
  git cherry-pick -m 1 $2
  git push origin $BRANCH:$BRANCH
done