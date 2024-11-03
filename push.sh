#!/bin/bash

# Run by entering the command './push.sh "commit message"'
# The commit message must be a string.
# Adds, commits, pulls, merges, pushes, checks out a new
# branch, and then deletes the fifth oldest branch
# in one command.
#
# Note: this script may not work if there is a merge conflict,
# which would have to be handled separately.

if [ $# -eq 1 ]; then
    branch_name=$(git branch --show-current)
    echo "$branch_name"
    
    if [ "$branch_name" = "main" ]; then
        git add . \
        && git commit -m "$1" \
        && git pull origin main --no-edit \
        && git push -u origin main \
        && echo -e "\nScript successful. Please checkout a non-main branch for the future" \
        || echo -e "\nScript unsuccessful"
    elif [ ${#branch_name} -gt 0 ]; then
        git add . \
        && git commit -m "$1" \
        && git switch -C draft \
        && git pull origin draft \
        && git merge "$branch_name" --no-edit \
        && git push -u origin draft \
        && echo -e "\nPush and merge successful" \
        && branch_beginning=$(echo "$branch_name" | awk -F. '{$NF=""; print $0}' OFS='.') \
        && last_version_number=$(echo "$branch_name" | awk -F. '{print $NF}') \
        && new_branch_last_version_number=$((last_version_number + 1)) \
        && new_branch_name="$branch_beginning$new_branch_last_version_number" \
        && git switch -C "$new_branch_name" \
        && echo "Checkout successful from $branch_name to $new_branch_name" \
        && fifth_oldest_branch_number=$((new_branch_last_version_number - 5)) \
        && fifth_oldest_branch_name="$branch_beginning$fifth_oldest_branch_number" \
        && git branch -d "$fifth_oldest_branch_name" &> /dev/null \
        && echo "Fifth oldest branch ($fifth_oldest_branch_name) successfully deleted" \
        || echo "Branch $fifth_oldest_branch_name not deleted (either not found or may be protected)" \
        && echo "Script successful" \
        || echo -e "\nScript unsuccessful"
    else
        echo "Error: Unable to retrieve the current branch name"
    fi
else
    echo "Please enter the commit message as a single string argument"
fi

