#!/bin/bash
# Uso: notify.sh "mensagem"
# Mostra uma notificação no Windows usando o PowerShell, sem instalar nada.

MSG="${1:-Claude Code}"
MSG="${MSG//\'/}"   # remove aspas simples pra não quebrar o comando do PowerShell

powershell.exe -NoProfile -Command "
  Add-Type -AssemblyName System.Windows.Forms;
  Add-Type -AssemblyName System.Drawing;
  \$n = New-Object System.Windows.Forms.NotifyIcon;
  \$n.Icon = [System.Drawing.SystemIcons]::Information;
  \$n.Visible = \$true;
  \$n.ShowBalloonTip(5000, 'Claude Code', '$MSG', 'Info');
  Start-Sleep -Seconds 6;
  \$n.Dispose()
" > /dev/null 2>&1 &

exit 0