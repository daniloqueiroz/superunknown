#!/usr/bin/python
# -*- coding: utf-8 -*-
from copy import copy
from datetime import datetime
import signal
from subprocess import call
import sys
import time
from threading import Thread
import os

try:
    from watchdog.events import FileSystemEventHandler
    from watchdog.observers import Observer
except ImportError:
    print "This program requires watchdog to run. Try 'pip install watchdog'"
    sys.exit(42)

_dir=os.path.join(os.getcwd(), 'src')
_wait_seconds=5
_gradle_cmd=['gradle', '--daemon', '--no-color']


def print_help():
    msg = """
Usage: run.py [testing|run]

    * testing:  watches for source file modifications and runs the tests
                automatically - for TDD;
    * server:   runs development server and watches for source file
                modifications, when detected builds the project.
    """
    print msg


def gradle_run(task):
    print '>> Gradle task running'
    cmd = copy(_gradle_cmd)
    cmd.append(task)
    call(cmd)


def setup_monitor(task):
    print '\n>>> File monitor running...'
    observer = Observer()
    observer.schedule(GradleRunnerEventHandler(gradle_task=task),
                      _dir, recursive=True)
    observer.start()


def setup_server():
    print "\n>>> Starting server"
    server = Thread(target=gradle_run, args=('run',))
    server.daemon = True
    server.start()
    return server


def setup_signal_handler(thread=None):
    print '>> Use Ctrl+C to exit'
    # register sinal handler
    signal.signal(signal.SIGINT, finalize)
    signal.pause()


def finalize(signal=None, frame=None):
    print '\n>>> Exiting...\n'
    sys.exit(0)


class GradleRunnerEventHandler(FileSystemEventHandler):
    last_modification = None

    def __init__(self, gradle_task='test'):
        self.gradle_task = gradle_task

    def on_any_event(self, event):
        now = datetime.utcnow()
        if self.wait_time_expired(now):
            self.last_modification = now
            gradle_run(self.gradle_task)

    def wait_time_expired(self, now):
        return ((now - self.last_modification).seconds > _wait_seconds
                if self.last_modification else True)


if __name__ == "__main__":
    command = sys.argv[1] if len(sys.argv) > 1 else 'help'
    if command == 'testing' or command == 'test':
        setup_monitor('test')
        setup_signal_handler()
    elif command == 'run' or command == 'server':
        server = setup_server()
        time.sleep(10)
        if server.is_alive():
            setup_monitor('war')
            setup_signal_handler(server)
        else:
            finalize()
    else:
        print_help()
        sys.exit(0)

