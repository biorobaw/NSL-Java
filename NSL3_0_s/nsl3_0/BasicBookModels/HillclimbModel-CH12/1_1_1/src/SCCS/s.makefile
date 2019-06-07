h28898
s 00035/00000/00000
d D 1.1 99/09/24 16:05:50 aalx 1 0
c date and time created 99/09/24 16:05:50 by aalx
e
u
U
f e 0
t
T
I 1
# SCCS @(#)makefile	1.1 --- 03/17/99 -- 14:22:21
# Generic makefile for a Model
# make sure to source the resume file before executing this file
# NSL_OS or Operating System must be defined.
# options: unix, win95, win98, nt
# IMPORTANT:
# This makefile uses the bash shell on the pc's (win95, 98, nt).
# The bash shell is available from the nsl installation site, but it
# and other cygnus utilities must be installed for this script to work.
# The cygnus tools are available from:
# ftp://go.cygnus.com/pub/sourceware.cygnus.com/cygwin/cygwin-b20/full.exe
# However, cygnus is planning to change their name soon. (99/3/17)
#----------------------------------

all:
	@if [ "$(NSL_OS)" = unix ]; then \
	makemake.unix; \
	make -f _make1.unix all; \
	make -f _make2.unix all; \
	else \
	bash makemake.nt; \
	make -f _make1.nt all; \
	make -f _make2.nt all; \
	fi
clean:
	@if [ "$(NSL_OS)" = unix ]; then \
	makemake.unix; \
	make -f _make1.unix clean; \
	make -f _make2.unix clean; \
	else \
	bash makemake.nt; \
	make -f _make1.nt clean; \
	make -f _make2.nt clean; \
	fi

E 1
