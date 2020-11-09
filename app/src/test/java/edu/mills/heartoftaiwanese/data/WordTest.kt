package edu.mills.heartoftaiwanese.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WordTest {

    @Test
    fun `detect all null`() {
        val w = Word()
        assertTrue(w.containsNull())
    }

    @Test
    fun `detect english null`() {
        val w = Word("something", null, null)
        assertTrue((w.containsNull()))
    }

    @Test
    fun `detect chinese null`() {
        val w = Word(chinese = "not null string")
        assertTrue((w.containsNull()))
    }

    @Test
    fun `detect taiwanese null`() {
        val w = Word(taiwanese = "dai gu")
        assertTrue((w.containsNull()))
    }

    @Test
    fun `detect none null`() {
        val w = Word("hippo", "he ma", "ho be")
        assertFalse(w.containsNull())
    }
}