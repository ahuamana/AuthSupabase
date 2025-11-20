package com.ahuaman.authsupabase.ui

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://xjxfqgfyhgshocbnxlxw.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeGZxZ2Z5aGdzaG9jYm54bHh3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjMyNTY4NzYsImV4cCI6MjA3ODgzMjg3Nn0.gv7FQFzWSCdA7jLL-AZdgT5YOasC8YzQ-cp6nwsPG7E"
) {
    install(Postgrest)
    //install other modules
}