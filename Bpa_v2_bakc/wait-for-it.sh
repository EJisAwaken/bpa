#!/usr/bin/env bash
# wait-for-it.sh

host="$1"
shift
cmd="$@"

until nc -z $(echo $host | cut -d: -f1) $(echo $host | cut -d: -f2); do
  >&2 echo "⏳ MySQL n'est pas encore prêt sur $host..."
  sleep 3
done

>&2 echo "✅ MySQL est prêt sur $host, lancement de l'application"
exec $cmd
