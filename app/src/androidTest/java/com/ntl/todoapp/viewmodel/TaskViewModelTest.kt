package com.ntl.todoapp.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.ntl.todoapp.getOrAwaitValue
import com.ntl.todoapp.model.Task
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskViewModelTest : TestCase() {
    private lateinit var viewModel: TaskViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testTodoViewModel() {
        val application = ApplicationProvider.getApplicationContext() as Application
        viewModel = TaskViewModel(application)
        viewModel.insertTask(Task(false, "Test todo viewmodel"));
        val result = viewModel.allTasks.getOrAwaitValue().find {
            !it.isComplete && it.name == "Test todo viewmodel"
        }
        assertThat(result != null).isTrue()
    }
}