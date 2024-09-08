<script lang="ts">
  const apiUrl = import.meta.env.VITE_BACKEND_API_URL;
  let userName = '';

  async function registerUser() {
    if (userName.trim() === '') {
      alert('User name cannot be empty');
      return;
    }

    try {
      const response = await fetch(`${apiUrl}/users`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: userName })
      });

      if (response.ok) {
        alert('User registered successfully');
        userName = ''; // フォームをクリア
      } else {
        console.error('Failed to register user:', response.status);
        alert('Failed to register user');
      }
    } catch (error) {
      console.error('Error registering user:', error);
      alert('Error registering user');
    }
  }
</script>

<h1>Register User</h1>

<form on:submit|preventDefault={registerUser}>
  <label for="userName">User Name:</label>
  <input type="text" id="userName" bind:value={userName} placeholder="Enter user name" />
  <button type="submit">Register</button>
</form>

<p>
  <a href="/users">View All Users</a>
</p>