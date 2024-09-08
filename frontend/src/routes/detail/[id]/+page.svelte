<script lang="ts">
  import { onMount } from 'svelte';
  import { page } from '$app/stores';
  import { goto } from '$app/navigation';
  const apiUrl = import.meta.env.VITE_BACKEND_API_URL;

  type User = {
    id: string;
    name: string;
  };

  let user: User;
  let id: string; // URLから取得したID
  let updatedName = '';

  // ユーザーを取得する関数
  async function fetchUser() {
    try {
      const response = await fetch(`${apiUrl}/users/${id}`);
      if (response.ok) {
        user = await response.json();
        updatedName = user.name; // フォームに現在の名前をプリセット
      } else {
        console.error('Failed to fetch user:', response.status);
      }
    } catch (error) {
      console.error('Error fetching user:', error);
    }
  }

  // ユーザーのnameを更新する関数
  async function updateUserName() {
    try {
      const response = await fetch(`${apiUrl}/users/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          id: user.id,  // idはそのままに
          name: updatedName, // nameのみを更新
        }),
      });
      if (response.ok) {
        alert('User name updated successfully');
        goto(`/detail/${id}`); // 更新後も同じIDのページにリダイレクト
      } else {
        console.error('Failed to update user name:', response.status);
      }
    } catch (error) {
      console.error('Error updating user name:', error);
    }
  }

  // ユーザーを削除する関数
  async function deleteUser() {
    try {
      const response = await fetch(`${apiUrl}/users/${id}`, {
        method: 'DELETE',
      });
      if (response.ok) {
        alert('User deleted successfully');
        goto('/users'); // 削除後にホームページへリダイレクト
      } else {
        console.error('Failed to delete user:', response.status);
      }
    } catch (error) {
      console.error('Error deleting user:', error);
    }
  }

  onMount(() => {
    id = $page.params.id;
    fetchUser();
  });
</script>

{#if user}
  <h1>Edit User Name</h1>

  <!-- 編集フォーム -->
  <form on:submit|preventDefault={updateUserName}>
    <div>
      <label for="id">ID: </label>
      <input type="text" id="id" value={user.id} readonly /> <!-- IDは表示のみに -->
    </div>
    <div>
      <label for="name">Name: </label>
      <input
        type="text"
        id="name"
        bind:value={updatedName}
      />
    </div>
    <button type="submit">Update Name</button>
  </form>

  <!-- 削除ボタン -->
  <button on:click={deleteUser}>Delete User</button>
{:else}
  <p>Loading...</p>
{/if}

<p>
  <a href="/users">View All Users</a>
</p>